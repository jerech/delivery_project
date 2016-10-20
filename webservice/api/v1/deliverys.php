<?php



$app->post('/SetdeviceToken', function() use ($app) {
   verifyRequiredParams(array('regId', 'email', 'typeUser'));     // Chequea que parametros existan
            $response = array();           

            $regId = $app->request->post('regId');
            $email = $app->request->post('email');
            $typeUser = $app->request->post('typeUser');

           $gcm = new GCM();
          $result =  $gcm->registerClient($regId, $typeUser, $email);    

    if ($result) {
        $response["error"] = false;
        $response["message"] = $result;

        echoRespnse(200, $response);
    } else {
        $response["error"] = true;
        $response["message"] = false;
        echoRespnse(404, $response);
    }
});

$app->post('/sandboxSendSms', function() use ($app) {
   verifyRequiredParams(array('regId'));     // Chequea que parametros existan
            $response = array();           

            $regId = $app->request->post('regId');
    $mensage = Array(
        "receiver"=>"manager",
        "type"=>"manager_decline_delivery",
        "title"=>"Notice: delivery not accepted",
        "id_delivery"=>1,
        "id_restaurant"=>1,
        "body"=>"PASDDGDGGSHD". MESSAGE_REVIEW_FOR_MANAGER);



 /*   $mensage = Array(
        "type"=>"new",
        "title"=>"New Delivery",
        "id_delivery"=>1,
        "id_restaurant"=>1,
        "body"=>"New service :DEMO");*/
           $gcm = new GCM();
          $result =  $gcm->sendMessageThroughGCM(Array($regId), Array("message" => $mensage));

    if ($result) {
        $response["error"] = false;
        $response["message"] =  $result ;
        echoRespnse(200, $response);
    } else {
        $response["error"] = true;
        $response["message"] =  $result ;
        echoRespnse(404, $response);
    }
});
$app->post('/sandboxSendSms2', function() use ($app) {
    verifyRequiredParams(array('regId'));     // Chequea que parametros existan
    $response = array();

    $regId = $app->request->post('regId');



     $mensage = Array(
         "receiver"=>"drivers",
           "type"=>"new",
           "title"=>"New Delivery",
           "id_delivery"=>1,
           "id_restaurant"=>1,
           "body"=>"New service :DEMO");
    $gcm = new GCM();
    $result =  $gcm->sendMessageThroughGCM(Array($regId), Array("message" => $mensage));

    if ($result) {
        $response["error"] = false;
        $response["message"] =  $result ;
        echoRespnse(200, $response);
    } else {
        $response["error"] = true;
        $response["message"] =  $result ;
        echoRespnse(404, $response);
    }
});
$app->post('/sandboxSendSmsAll', function() use ($app) {
   verifyRequiredParams(array( 'sms'));     // Chequea que parametros existan
            $response = array();          

            $sms = $app->request->post('sms');


           $gcm = new GCM();
          $result =  $gcm->setSmsAllDrivers( Array("message" => $sms));

    if ($result) {
        $response["error"] = false;
        $response["message"] = $result;
        echoRespnse(200, $response);
    } else {
        $response["error"] = true;
        $response["message"] = $result;
        echoRespnse(404, $response);
    }
});




//___________________________________________________________________________________________________
// DELIVERY DELIVERY DELIVERY DELIVERY DELIVERY DELIVERY DELIVERY DELIVERY DELIVERY DELIVERY
//___________________________________________________________________________________________________

// DELIVERIES FOR RESTAURANT ID
$app->get('/deliverys/:id', function($id)  {
            //global $user_id;
  
            $response = array();
            $db = new HandlerDb();

            // fetch task
            $result2 = $db->getDeliverys($id);


            if ($result2 != NULL) {
                   echoRespnse(200, $result2);
            } else {
                $response["error"] = true;
                $response["message"] = "The requested resource doesn't exists!!!";
                echoRespnse(404, $response);
            }
});
$app->get('/deliverysHistory/:id', function($id)  {
    //global $user_id;

    $response = array();
    $db = new HandlerDb();

    // fetch task
    $result2 = $db->getDeliverysHistory($id);


    if ($result2 != NULL) {
        echoRespnse(200, $result2);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }
});
$app->get('/deliverysHistory2/:id/:start_date/:end_date', function($id,$start_date,$end_date)  {
    //global $user_id;

    $response = array();
    $db = new HandlerDb();

    // fetch task
    $result2 = $db->getDeliverysHistory2($id,$start_date,$end_date);


    if ($result2 != NULL) {
        echoRespnse(200, $result2);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }
});
$app->get('/deliverysHistoryDriver/:id', function($id)  {
    //global $user_id;

    $response = array();
    $db = new HandlerDb();

    // fetch task
    $result2 = $db->getDeliverysHistoryDriver($id);

    if ($result2 != NULL) {
        echoRespnse(200, $result2);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }
});

$app->get('/deliverysHistoryDriver2/:id/:start_date/:end_date', function($id,$start_date,$end_date)  {
    //global $user_id;

    $response = array();
    $db = new HandlerDb();

    // fetch task
    $result2 = $db->getDeliverysHistoryDriver2($id,$start_date,$end_date);

    if ($result2 != NULL) {
        echoRespnse(200, $result2);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }
});
$app->get('/deliverysHistoryManager/:start_date/:end_date', function($start_date, $end_date)  {
    //global $user_id;

    $db = new HandlerDb();

   // verifyRequiredParams(array( 'start_date','end_date'));     // Chequea que parametros existan

    $response = array();
    // Generar Paamatros
   // $end_date = $app->request->post('end_date');
   // $start_date = $app->request->post('start_date');

    // fetch task
    $result2 = $db->getDeliverysHistoryManager($start_date,$end_date);


    if ($result2 != NULL) {
        echoRespnse(200, $result2);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }
});



$app->get('/deliveriesFree', function()  {
     $response = array();
    $db = new HandlerDb();

    // fetch task
    $result2 = $db->getDeliverysFree();


    if ($result2 != NULL) {
        echoRespnse(200, $result2);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }


});

$app->get('/deliveriesFree2', function()  {
     $response = array();
    $db = new HandlerDb();

    // fetch task
    $result2 = $db->getDeliverysFree();


    if ($result2 != NULL) {
        echoRespnse(200, $result2);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(200, array());
    }


});


$app->get('/deliveriesFreeCount', function()  {
    $response = array();
    $db = new HandlerDb();

    // fetch task
    $result2 = $db->getDeliverysFreeCount();


    if ($result2 != NULL) {
        echoRespnse(200, $result2);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }


});

$app->get('/delivery/:id', function($id)  {
            //global $user_id;
  
            $response = array();
            $db = new HandlerDb();

            // fetch task
            $result = $db->getDelivery($id);

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

$app->get('/deliveryDetail/:id', function($id)  {
    //global $user_id;

    $response = array();
    $db = new HandlerDb();

    // fetch task
    $result = $db->getDeliveryDetail($id);

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


$app->get('/noticeDelivery/:id', function($id){

    $response = array();
    $db = new HandlerDb();

    // fetch task
    $result = $db->getNoticeDelivery($id);

    if ($result != NULL) {
        echoRespnse(200, $result);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }


});

$app->post('/delivery', function() use ($app) {
            verifyRequiredParams(array('address', 'note', 'id_restaurant'));     // Chequea que parametros existan
            // Generar Paamatros
            $address = $app->request->post('address');
            $note = $app->request->post('note');
            $id_restaurant = $app->request->post('id_restaurant');

            $db = new HandlerDb();
            $res = $db->createDelivery($address, $note, $id_restaurant);

            if ($res != null) {
                $response = array("id_delivery"=>$res);
                echoRespnse(200, $response);

                $resultRestaurant =$db->getRestaurant($id_restaurant);

                $mensage = Array(
                    "receiver"=>"driver",
                    "type"=>"new",
                    "title"=>"New Delivery",
                    "id_delivery"=>$res,
                    "id_restaurant"=>$id_restaurant,
                    "body"=>"New service :" . $resultRestaurant[0]["name"] . " Address : " . $resultRestaurant[0]['address']);



                $mensage2 = Array(
                    "receiver"=>"driver",
                    "type"=>"new_all",
                    "title"=>"New Delivery",
                    "id_delivery"=>$res,
                    "id_restaurant"=>$id_restaurant,
                    "body"=>"New service :" . $resultRestaurant[0]["name"] . " Address : " . $resultRestaurant[0]['address']);

                $gcm = new GCM();
                //$gcm->generateAlertsPushOnDriversClients($mensage, $res, $mensage2 );

                $gcm->generateAlertsPushOnDriversClientsV2($mensage2);

            } else  {
                $response = array();
                $response["error"] = true;
                $response["message"] = "Oops! An error occurred while registereing";
                echoRespnse(401, $response);
            }
});

$app->post('/delivery/:id', function($id) use ($app)  {
    verifyRequiredParams(array('address', 'note','id_driver','time_driver'));     // Chequea que parametros existan

    $response = array();
    // Generar Paamatros

    $address = $app->request->post('address');
    $note = $app->request->post('note');
    $id_driver = $app->request->post('id_driver');
    $time_driver = $app->request->post('time_driver');

    $db = new HandlerDb();
    $result = $db->updateDelivery($id, $address, $note, $id_driver, $time_driver);

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
/*
 *  FINISH ONE DELIVERY ACTIVE
 */
$app->post('/FinishDeliveryPut', function() use ($app)  {
    verifyRequiredParams(array( 'id','id_driver'));     // Chequea que parametros existan

    $response = array();
    // Generar Paamatros
    $id = $app->request->post('id');
    $id_driver = $app->request->post('id_driver');

    $db = new HandlerDb();
    $result = $db->FinishDelivery($id, $id_driver);

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
// $app->put
$app->post('/updateTimeDeliverys', function() use ($app)  {
   verifyRequiredParams(array('data'));     // Chequea que parametros existan

    $response = array();
    // Generar Paamatros

    $jsondata = $app->request->post('data');

    $data = json_decode($jsondata, true);


    $db = new HandlerDb();
    $result = $db->updateDeliveryTime($data);

    if ($result != NULL) {
        $response["error"] = false;
        $response["main"] = array();
       echoRespnse(200, $result);
    } else {
        $response["error"] = true;
        $response["message"] = "The requested resource doesn't exists!!!";
        echoRespnse(404, $response);
    }
});


$app->post('/deliveryDelete/:id', function($id)  {
    $db = new HandlerDb();
    $res = $db->deleteDeliery($id);

        $response = array("message"=>"ok");
        echoRespnse(200, $response);
});

$app->post('/sniffNotify', function() use ($app) {
            verifyRequiredParams(array('id_delivery', 'id_restaurant'));     // Chequea que parametros existan

            $response = array();
            // Generar Paamatros
            $id_delivery = $app->request->post('id_delivery');
            $id_restaurant = $app->request->post('id_restaurant');

            $db = new HandlerDb();
            $result = $db->getDeliverysNotificationsLost($id_delivery);
            $result2 = $db->getDeliverysNotificationsLostV2($id_delivery);

            if ($result != null OR $result2 == null ) { // Notify  to manager

                //DELETE ALL LOST NOTIFY
                $db->deletePushTmpListAll($id_delivery);

                //GET RESTAURANT
                $restaurant = $db->getRestaurant($id_restaurant);

                $mensage = Array(
                    "receiver"=>"manager",
                    "type"=>"manager_decline_delivery",
                    "title"=>"Notice: delivery not accepted",
                    "id_delivery"=>$id_delivery,
                    "id_restaurant"=>$id_restaurant,
                    "body"=>$restaurant[0]["name"]. MESSAGE_REVIEW_FOR_MANAGER);


                $gcm = new GCM();
                $gcm->generateAlertsPushNoticeDeniedToRestaurantToMnager($mensage);

            }
                $response["error"] = false;
                echoRespnse(200, $response);

});