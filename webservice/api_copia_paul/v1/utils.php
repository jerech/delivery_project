<?php

$app->post('/pushResposeDriver', function() use ($app) {
            verifyRequiredParams(array(
                'device_id',
                'responseAccept',
                'id_driver',
                'id_delivery',
                'timer_driver',
                'id_restaurant'    ));     // Chequea que parametros existan

    $response = array();
    // Generar Paamatros
    $device_id = $app->request->post('device_id');
    $responseAccept = $app->request->post('responseAccept');
    $id_driver = $app->request->post('id_driver');
    $id_delivery = $app->request->post('id_delivery');
    $timer_driver = $app->request->post('timer_driver');
    $id_restaurant = $app->request->post('id_restaurant');

            $db = new HandlerDb();
            
            if($responseAccept == "true"){
                $db->deletePushTmpListAll($id_delivery);
                $response["error"] = false;
                $response["message"] = "successfully";
                echoRespnse(201, $response);

                //$tmpDelivery = $db->getDelivery($id_delivery);

                $tmpDriver = $db->getDriver($id_driver);

                $name = $tmpDriver[0]["first_name"] . " " . $tmpDriver[0]["last_name"];

                $db->setUpdateDeliveryDriverAccept($id_delivery,$id_driver, $name , $timer_driver);

            }else{
                $resultRestaurant =$db->getRestaurant($id_restaurant);
                $mensage = Array(
                    "receiver"=>"driver",
                    "type"=>"new",
                    "title"=>"New Delivery",
                    "id_delivery"=>$id_delivery ,
                    "id_restaurant"=>$id_restaurant,
                    "body"=>"New service :" . $resultRestaurant[0]["name"] . " Address : " . $resultRestaurant[0]['address']);

               // $db->deletePushTmpList($device_id,$id_delivery);
                $gcm = new GCM();
                $gcm->nextGenerateAlertsPushOnDriversClients($mensage, $id_delivery, $id_restaurant );
                $response["error"] = false;
                $response["message"] = "Next Driver";
                echoRespnse(201, $response);
            }

           // echoRespnse(201, $response);
});



//___________________________________________________________________________________________________
// MANAGER
//___________________________________________________________________________________________________

$app->post('/registerManager', function() use ($app) {        
            verifyRequiredParams(array('email','password'));     // Chequea que parametros existan

            $response = array();
            // Generar Paamatros
            $email = $app->request->post('email');
            $password = $app->request->post('password');
           
            validateEmail($email); // si el email es valido

            $db = new HandlerDb();
            $res = $db->createManager($email, $password );

            if ($res == USER_CREATED_SUCCESS) {

                $response["error"] = false;              
                $response["message"] = "You are successfully registered";
                

            } else if ($res == USER_CREATE_FAILED) {
                $response["error"] = true;
                $response["message"] = "Oops! An error occurred while registereing";
            } else if ($res == USER_ALREADY_EXISTED) {
                $response["error"] = true;
                $response["message"] = "Sorry, this email already existed";
            }
            // echo json response
            echoRespnse(201, $response);
});

$app->post('/loginManager', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('email', 'password'));

            // reading post params
            $email = $app->request()->post('email');
            $password = $app->request()->post('password');
            $response = array();

            $db = new HandlerDb();
            // check for correct email and password
            if ($db->checkLoginManager($email, $password)) {
                // get the user by email
                $user = $db->getManagerByEmail($email);


                        echoRespnse(200, $user);

            } else {
                // user credentials are wrong
                $response['error'] = true;
                $response['message'] = 'Login failed. Incorrect credentials';
                 echoRespnse(401, $response);
            }
           
});

$app->post('/logoutManager', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('email'));
            // reading post params
            $email = $app->request()->post('email');
 
            $response = array();
            $db = new HandlerDb();
            // check for correct email and password
            $db->setManagerLogout($email);
                $response['error'] = false;
                $response['message'] = 'Logout sucsessed';
                $gcm = new GCM();
               $gcm->unregisterClient($email, "3") ;


            echoRespnse(200, $response);
});

$app->post('/offline', function() use ($app) {
    // check for required params
    $id= $app->request()->post('id');
    $response = array();
    $db = new HandlerDb();
    // check for correct email and password
    $db->setOffLine($id);

    $response['error'] = false;
    $response['message'] = 'sucsessed';


    echoRespnse(200, $response);
});

$app->post('/online', function() use ($app) {
    // check for required params
    $device_id = $app->request()->post('device_id');
    $id= $app->request()->post('id');


    $response = array();
    $db = new HandlerDb();
    // check for correct email and password
    $db->setOnLine($id, $device_id);

    $response['error'] = false;
    $response['message'] = 'sucsessed';


    echoRespnse(200, $response);
});
/*
 * RESET PASS DRIVER
 */
$app->get('/DriverRestorePassword/:id', function($id) use ($app) {
    $email = $id;
    $response = array();
    $db = new HandlerDb();
    // check for correct email and password
    $api_key = $db->getRestorePassword($email);

    sendEmailResetPassDriver($email, $api_key );

    $response['error'] = false;
    $response['message'] = 'send email';
      echoRespnse(200, $response);
});
$app->get('/DriverRestore/', function() use ($app) {
    $app->render('/restore.php', array('id' => ''));
});
$app->post('/DriverNewPass', function() use ($app) {
    // check for required params
    $token = $app->request()->post('token');
    $email = $app->request()->post('email');
    $password = $app->request()->post('password');
    $password2 = $app->request()->post('password2');

    if($password == $password2){
        $db = new HandlerDb();
        // check for correct email and password
        $result = $db->getRestorePasswordNew($email, $password, $token);
        $app->render('/finish.php', array('id' => "Password changed successfully, close this window and try to enter again in the application"));
    }else{
        $app->render('/finish.php', array('id' => "Error password"));
    }
});

/*
 * RESET PASS RESTAURANT
 */
$app->get('/RestaurantRestorePassword/:id', function($id) use ($app) {
    $email = $id;
    $response = array();
    $db = new HandlerDb();
    // check for correct email and password
    $api_key = $db->getRestorePasswordRestaurat($email);

    sendEmailResetPassRestaurant($email, $api_key );

    $response['error'] = false;
    $response['message'] = 'send email';
    echoRespnse(200, $response);
});
$app->get('/RestaurantRestore/', function() use ($app) {
    $app->render('/restaurantrestore.php', array('id' => ''));
});
$app->post('/RestaurantNewPass', function() use ($app) {
    // check for required params
    $token = $app->request()->post('token');
    $email = $app->request()->post('email');
    $password = $app->request()->post('password');
    $password2 = $app->request()->post('password2');

    if($password == $password2){
        $db = new HandlerDb();
        // check for correct email and password
        $result = $db->getRestorePasswordNewRestaurant($email, $password, $token);
        $app->render('/finish.php', array('id' => "Password changed successfully, close this window and try to enter again in the application"));
    }else{
        $app->render('/finish.php', array('id' => "Error password"));
    }
});