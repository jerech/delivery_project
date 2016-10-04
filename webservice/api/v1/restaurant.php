<?php


$app->post('/registerRestaurant', function() use ($app) {        
            verifyRequiredParams(array('name', 'address', 'address','phone','email','password','image_url' ));     // Chequea que parametros existan
            $response = array();
            // Generar Paamatros
            $name = $app->request->post('name');
            $address = $app->request->post('address');
            $phone = $app->request->post('phone');
            $email = $app->request->post('email');
            $password = $app->request->post('password');
            $image_url = $app->request->post('image_url'); 

            validateEmail($email); // si el email es valido

            $db = new HandlerDb();
            $res = $db->createRestaurant($name, $address, $phone, $email, $password );

            if ($res != null) {
                save_base64_image($image_url, PATCH_TO_IMAGES_RESTAURANT .  $res['id']);

                $response["error"] = false;
                $response["message"] = "You are successfully registered";
                echoRespnse(200, $response);
            } else if ($res == USER_CREATE_FAILED) {
                $response["error"] = true;
                $response["message"] = "Oops! An error occurred while registereing";
                echoRespnse(400, $response);
            } 
            // echo json response

});

$app->post('/loginRestaurant', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('email', 'password'));

            // reading post params
            $email = $app->request()->post('email');
            $password = $app->request()->post('password');
            $deviceId = $app->request()->post('deviceId');
            $response = array();

            $db = new HandlerDb();
            // check for correct email and password
            if ($db->checkLoginRestaurant($email, $password)) {
                // get the user by email
                $user = $db->getRestaurantByEmail($email);

                if ($user != NULL) {
                        echoRespnse(200, $user);
                } else {
                    // unknown error occurred
                    $response['error'] = true;
                    $response['message'] = "An error occurred. Please try again";
                    echoRespnse(401, $response);
                }
            } else {
                // user credentials are wrong
                $response['error'] = true;
                $response['message'] = 'Login failed. Incorrect credentials';
                 echoRespnse(401, $response);
            }

           
});

$app->post('/logoutRestaurant', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('email'));

            // reading post params
            $email = $app->request()->post('email');

            $response = array();

            $db = new HandlerDb();
            // check for correct email and password
            $db->setRestaurantLogout($email);
                $gcm = new GCM();
                $resx =  $gcm->unregisterClient($email, "1") ;
                $response['error'] = false;
                $response['message'] = 'Logout sucsessed';
              //  $response["gcm"] = $resx;


            echoRespnse(200, $response);
});

$app->get('/restaurantList', function() {
            //global $user_id;
  
            $response = array();
            $db = new HandlerDb();

            // fetch task
            $result = $db->getRestaurantList();

            if ($result != NULL) {
                echoRespnse(200, $result);
            } else {
                $response["error"] = true;
                $response["message"] = "The requested resource doesn't exists!!!";
                echoRespnse(404, $response);
            }

            
});

$app->get('/restaurantList2', function() {
            //global $user_id;
  
            $response = array();
            $db = new HandlerDb();

            // fetch task
            $result = $db->getRestaurantList2();

            if ($result != NULL) {
                echoRespnse(200, $result);
            } else {
                $response["error"] = true;
                $response["message"] = "The requested resource doesn't exists!!!";
                echoRespnse(404, $response);
            }

            
});

$app->get('/restaurant', function($id) {
    //global $user_id;

    $response = array();
    $db = new HandlerDb();

    // fetch task
    $result = $db->getRestaurant($id);

    if ($result != NULL) {
        $response["error"] = false;
        $response["main"] = array();
        array_push($response["main"], $result);
        echoRespnse(200, $result);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }


});

$app->get('/restaurant/:id', function($id) {
            //global $user_id;
  
            $response = array();
            $db = new HandlerDb();

            // fetch task
            $result = $db->getRestaurant($id);

            if ($result != NULL) {
                $response["error"] = false;       
                $response["main"] = array();
                 array_push($response["main"], $result);
                echoRespnse(200, $result);
            } else {
                $response["error"] = true;
                $response["message"] = "The requested resource doesn't exists!!!";
                echoRespnse(404, $response);
            }

            
});

$app->post('/restaurantPut/:id', function($id) use ($app)  {
    verifyRequiredParams(array('name',  'address','phone','email', 'image_url'));     // Chequea que parametros existan

    $response = array();
    // Generar Paamatros
    $name = $app->request->post('name');
    $address = $app->request->post('address');
    $phone = $app->request->post('phone');
    $email = $app->request->post('email');
    $image_url = $app->request->post('image_url');
    validateEmail($email); // si el email es valido

    $db = new HandlerDb();
    $db->updateRestaurant($id, $name,  $address, $phone, $email );
    $result = $db->getRestaurantByEmail($email);
    if ($result != null) {
        save_base64_image($image_url, PATCH_TO_IMAGES_RESTAURANT . $result["id"]);
        $response["error"] = false;
        $response["main"] = array();
        array_push($response["main"], $result);
        echoRespnse(200, $result);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }


});
