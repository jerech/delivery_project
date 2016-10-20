<?php 

$app->post('/registerDriver', function() use ($app) {        
            verifyRequiredParams(array('first_name', 'last_name', 'address','phone','email','password','image_url'));     // Chequea que parametros existan

            $response = array();
            // Generar Paamatros
            $first_name = $app->request->post('first_name');
            $last_name = $app->request->post('last_name');
            $address = $app->request->post('address');
            $phone = $app->request->post('phone');
            $email = $app->request->post('email');
            $password = $app->request->post('password');
            $image_url = $app->request->post('image_url');
            validateEmail($email); // si el email es valido
            $db = new HandlerDb();
            $res = $db->createDriver($first_name, $last_name,  $address, $phone, $email, $password  );

            if ($res != null) {

             save_base64_image($image_url, PATCH_TO_IMAGES_DRIVER .  $res['id']);

                $response["error"] = false;              
                $response["message"] = "You are successfully registered";
                echoRespnse(200, $response);

            } else  {
                $response["error"] = true;
                $response["message"] = "Oops! An error occurred while registereing";
                echoRespnse(400, $response);
            } 
            
});

$app->post('/loginDriver', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('email', 'password'));

            // reading post params
            $email = $app->request()->post('email');
            $password = $app->request()->post('password');
           // $deviceId = $app->request()->post('deviceId');
            $response = array();

            $db = new HandlerDb();
            // check for correct email and password
            if ($db->checkLoginDriver($email, $password)) {
                    

                // get the user by email
                $user = $db->getDriverByEmail($email);

                if ($user != NULL) {
                 

                    echoRespnse(200, $user);

                } else {
                    // unknown error occurred
                    $response['error'] = true;
                    $response['message'] = "An error occurred. Please try again";
                    echoRespnse(404, $user);
                }
            } else {
                // user credentials are wrong
                $response['error'] = true;
                $response['message'] = 'Login failed. Incorrect credentials';
                echoRespnse(404, $response);
            }

            
        });

$app->post('/logoutDriver', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('email'));

            // reading post params
            $email = $app->request()->post('email');
 
            $response = array();

            $db = new HandlerDb();
            // check for correct email and password
            $db->setDriverLogout($email);
               $gcm = new GCM();
               $gcm->unregisterClient($email, "2") ;

                $response['error'] = false;
                $response['message'] = 'Logout sucsessed';
                //$response["gcm"] = $resx;


            echoRespnse(200, $response);
});

$app->get('/driver/:id', function($id) {
              $response = array();
            $db = new HandlerDb();

            $result = $db->getDriver($id);

            if ($result != NULL) {
                echoRespnse(200, $result[0]);
            } else {
                $response["error"] = true;
                $response["message"] = "The requested resource doesn't exists!!!";
                echoRespnse(404, $response);
            }
            
});
$app->get('/driverListBlock', function() {
    $response = array();
    $db = new HandlerDb();

    $result = $db->getDriverListBlock();

    if ($result != NULL) {
        echoRespnse(200, $result);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }

});
$app->post('/BlockUser', function() use ($app)  {
    verifyRequiredParams(array('id'));
    $id = $app->request->post('id');
    $response = array();
    $db = new HandlerDb();

    $result = $db->setBlock($id);

    if ($result != NULL) {
        echoRespnse(200, $result);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }

});

$app->post('/driverPut/:id', function($id) use ($app)  {
    verifyRequiredParams(array('first_name', 'last_name', 'address','phone','email', 'image_url'));     // Chequea que parametros existan

    $response = array();
    // Generar Paamatros
    $first_name = $app->request->post('first_name');
    $last_name = $app->request->post('last_name');
    $address = $app->request->post('address');
    $phone = $app->request->post('phone');
    $email = $app->request->post('email');
    $image_url = $app->request->post('image_url');

    validateEmail($email); // si el email es valido
    $imageBool=false;
    if($image_url != ""){
        $imageBool= true;
    }
    $db = new HandlerDb();
    $result = $db->updateDriver($id, $first_name, $last_name,  $address, $phone, $email, $imageBool );
    $user = $db->getDriverByEmail($email);
    if ($user != NULL) {
        save_base64_image($image_url, PATCH_TO_IMAGES_DRIVER . $user["id"]);
        echoRespnse(200, $user);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }

});

$app->get('/driverImage/:id',  function($id) {
            $response = array();
            $db = new HandlerDb();
            // fetch task
            $result = $db->getDriverImageById($id);
            if ($result != NULL) {
                $base =$_SERVER['HTTP_HOST'] ;
                $base2 = str_replace("/index.php", "", $_SERVER['PHP_SELF'] );
                $path = $base . $base2 . $result['image_url'];

                echoRespnse(200, $path);
            } else {
                $response["error"] = true;
                $response["message"] = "The requested resource doesn't exists!!!";
                echoRespnse(404, $response);
            }

            
});

$app->get('/driverManagerProfile',  function() {
    $response = array();
    $db = new HandlerDb();
    // fetch task
    $result = $db->getDriverManagerArray();
    if ($result != NULL) {

        echoRespnse(200, $result);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }
});



$app->get('/driverList/:id', function($id) {
    $response = array();
    $db = new HandlerDb();

    $result = $db->getDriverList($id);

    if ($result != NULL) {
        echoRespnse(200, $result);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }

});
$app->get('/driverListDetail/:id', function($id) {
    $response = array();
    $db = new HandlerDb();

    $result = $db->getDriverListDetail($id);

    if ($result != NULL) {
        echoRespnse(200, $result);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }

});

$app->get('/driversWorkingList', function() {
    $response = array();
    $db = new HandlerDb();

    $result = $db->getDriverWorkingList();

    if ($result != NULL) {
        echoRespnse(200, $result);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }

});

$app->get('/driversWorkingList2', function() {
    $response = array();
    $db = new HandlerDb();

    $result = $db->getDriverWorkingList2();

    if ($result != NULL) {
        echoRespnse(200, $result);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }

});


$app->post('/driversChooseDelivery', function() use ($app) {

    verifyRequiredParams(array('id_delivery', 'id_driver', 'name_driver','timer_driver'));
    $id_delivery = $app->request->post('id_delivery');
    $id_driver= $app->request->post('id_driver');
    $name_driver = $app->request->post('name_driver');
    $timer_driver = $app->request->post('timer_driver');

    $response = array();
    $db = new HandlerDb();

    $result = $db->setChooseDelivery($id_delivery, $id_driver, $name_driver, $timer_driver);

    if ($result == true) {
        $response["error"] = false;
        $response["message"] = "Assigned resource ok";
        echoRespnse(200, $response);
    } else {
        $response["error"] = true;
        $response["message"] = "Resource already assigned";
        echoRespnse(404, $response);
    }


});

 ?>