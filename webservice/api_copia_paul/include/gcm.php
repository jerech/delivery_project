<?php 
/**
 * Implementacion de clase para notificaciones push
 * By PAUL OSINGA (PAULPWO@GMAIL.COM)
 * JUE- 30-JUN-2016
 *  DERECHOS PRIVADOS.
 */

include_once dirname(__FILE__) . '/configuration.php';


class GCM {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/Db.php';
        require_once dirname(__FILE__) . '/HandlerDb.php';
        // opening db connection
        $db = new Db();
        $this->conn = $db->connect();
    }

    public function registerClient($deviceId, $typeUser, $email){
        //$this->conn->query("SET NAMES utf8");
        if($typeUser == "1" ){
            $sqll = "UPDATE  restaurants SET status = 1 , device_id = ? WHERE email = ?";
        }
        if($typeUser == "2" ){
            $sqll = "UPDATE  drivers SET status = 1, device_id = ? WHERE email = ?";
        }
       /* if($typeUser == "3" ){
            $sqll = "UPDATE  manager SET device_id = ? WHERE email = ?";
        }*/
        

         $stmt = $this->conn->prepare($sqll);
         if ($stmt->execute(array( $deviceId, $email))) {
                // User successfully 
                return true;
            } else {
                // Failed
                return false;
            }
    }

    public function unregisterClient($email, $typeAccount){
        //$this->conn->query("SET NAMES utf8");
        if($typeAccount == "1"){
            $sqll = "UPDATE  restaurants SET device_id = '' WHERE email = '$email'";
        }
        if($typeAccount == "2"){
             $sqll = "UPDATE  drivers SET device_id = '' WHERE email = '$email'";
        }
       /* if($typeAccount == "3"){
            $sqll = "UPDATE  manager SET device_id = '' WHERE email = '$email'";
        }*/
          $stmt = $this->conn->prepare($sqll);
          return $stmt->execute();
            
    }


    // $deviceId $gcmRegIds = array($gcmRegID);
   public function sendMessageThroughGCM($deviceId, $message) {
        //Google cloud messaging GCM-API url
        $url = 'https://fcm.googleapis.com/fcm/send';
        $fields = array(
            'registration_ids' => $deviceId,
            'data' => $message,
        );
        // Update your Google Cloud Messaging API Key
            
        $headers = array(
            'Authorization: key=' . GOOGLE_API_KEY,
            'Content-Type: application/json'
        );
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);   
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        $result = curl_exec($ch);               
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }
        curl_close($ch);
        return $result;  // PUSH STATUS 
    }

    public function setSmsAllDrivers($sms){
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt1 = $this->conn->prepare("SELECT device_id FROM drivers WHERE device_id <>'' " );
        $stmt1->execute();
        $result = $stmt1->fetchAll(PDO::FETCH_ASSOC);

        $token = Array();

       foreach($result as $posicion=>$row)
        {          
               $token[] = $row['device_id'];    
            
        }
        $stmt = $this->conn->prepare("SELECT device_id FROM restaurants" );
        $stmt->execute();
        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
       foreach($result as $posicion=>$row)
        {
            array_push($token, $row['device_id'] );
            
        }



       return $this->sendMessageThroughGCM($token, $sms);



    }
    public function checkSendSMS($device_id){
        //$this->conn->query("SET NAMES utf8");
            //DELETE FROM push_tmp   WHERE notification_sent = 1  AND id_delivery = '$id_delivery' "
            //$sqll = "UPDATE  push_tmp SET notification_sent = 1 WHERE device_id = '$device_id'";

        $sqll = "DELETE FROM push_tmp   WHERE  id_delivery = '$device_id' ";

        $stmt = $this->conn->prepare($sqll);
        return $stmt->execute();
    }

    public function generateAlertsPushOnDriversClients($paramsend, $id_delivery, $paramAll){
        $this->getArrayClients($id_delivery);
        $device = $this->getDeviceForPush($id_delivery);
        if(isset($device)){

            $this->sendMessageThroughGCM(Array($device),Array("message" => $paramsend));



            $this->generateAlertsPushOnDriversClientsV2($paramAll);


            // $this->checkSendSMS($device);

        }
        //$this->schedule_DB($id_delivery);
        
    }

     public function nextGenerateAlertsPushOnDriversClients($paramsend, $id_delivery, $id_restaurant){
        $device = $this->getDeviceForPush($id_delivery);
        if(isset($device)){
           $this->sendMessageThroughGCM(Array($device),Array("message" => $paramsend));
        }else{
            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
            //SEND NOTICE TO MANAGER DELIVERY NOT ACCEPTANCE BY DRIVERS
            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
            //DELETE ALL LOST NOTIFY

            $db = new HandlerDb();
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

            $this->generateAlertsPushNoticeDeniedToRestaurantToMnager($mensage);


        }
    }

    private function getDeviceForPush($id_delivery){
         $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt1 = $this->conn->prepare("SELECT device_id FROM push_tmp
                                        WHERE id_delivery = '$id_delivery' AND notification_sent = 0 LIMIT 1" );
        $stmt1->execute();
        $result = $stmt1->fetchAll(PDO::FETCH_OBJ);
        if($result !=null){
            // CONTAR O VERIFICAR RESULTADO PARA SIGUIENTE NOTIFICACION
            $num_rows =$result[0]->device_id;
            if ($num_rows != "") {
                //siguiente notificacion

                //BORRAR PARA EVITAR REPETICION
                $stmt1 = $this->conn->prepare("DELETE FROM push_tmp   WHERE  device_id = '$num_rows'" );
                $stmt1->execute();
                 //RETORNA RESULTADO INDIVIDUAL

                return $num_rows;
            }
        }else{
            return null;
        }

    }

    private function getArrayClients($id_delivery){
         $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );

        $sql = "SELECT device_id FROM drivers  WHERE status = 1 AND working = 0 AND device_id != ''
            ORDER BY date_online ASC
            LIMIT ". LIMIT_USER_FOR_NOTIFY ;

        $stmt1 = $this->conn->prepare($sql );
        $stmt1->execute();
        $result = $stmt1->fetchAll(PDO::FETCH_OBJ);

         $stmt = $this->conn->prepare("INSERT INTO push_tmp  (device_id, id_delivery) VALUES (:name, :id) ");
        for($i = 0; $i < count($result); $i++) {
            $tmp = $result[$i]->device_id;
            $stmt->bindValue(':name', $tmp, PDO::PARAM_STR);
            $stmt->bindValue(':id', $id_delivery, PDO::PARAM_STR);
            $stmt->execute();
        }
    }


    private function getArrayClientsV2(){
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );

        $sql = "SELECT device_id FROM drivers  WHERE status = 1" ;

        $stmt1 = $this->conn->prepare($sql );
        $stmt1->execute();
        $result = $stmt1->fetchAll(PDO::FETCH_OBJ);
        $tmp = Array();
        for($i = 0; $i < count($result); $i++) {
            $tmp[$i] = $result[$i]->device_id;
        }
        return $tmp;
    }

    public function generateAlertsPushOnDriversClientsV2($paramsend){
        $device = $this->getArrayClientsV2();
        if(isset($device)){
            $this->sendMessageThroughGCM($device, Array("message" => $paramsend));
        }
    }



    /*
     * Functions for Restautans
     *
     */
    public function generateAlertsPushNoticeDeniedToRestaurantToMnager($sms){
        $stmt = $this->conn->prepare("SELECT device_id FROM drivers WHERE manager = 1" );
        $stmt->execute();
        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
        $token = Array();

        foreach($result as $posicion=>$row)
        {
            array_push($token, $row['device_id'] );

        }

        $this->sendMessageThroughGCM($token, Array("message" =>$sms));
    }


}
