<?php


class HandlerDb {

    private $conn;
//_________________________________________________________________________________________________
    function __construct() {
        require_once dirname(__FILE__) . '/Db.php';
        // opening db connection
        $db = new Db();
        $this->conn = $db->connect();
    }
//__________________________________________________________________________________________________

//___________________________________________________________________________________________________
    /**
     * Creacion de un usuario
     * @param String $name User full name
     * @param String $email User login email id
     * @param String $password User login password
     */
    public function createDriver($first_name, $last_name,  $address, $phone, $email, $password ) {
        require_once 'PassHash.php';
        $response = array();

        // Verifica que usuario no exista ya en db
        if (!$this->isDriversExists($email)) {
            
            $password_hash = PassHash::hash($password); //password hash            

            $this->conn->query("SET NAMES utf8");
            $stmt = $this->conn->prepare("INSERT INTO `drivers` (
            `first_name`,
             `last_name`, 
             `address`, 
             `phone`, 
             `email`, 
             `password_hash`, 
             `status`) values(?, ?, ?, ?,?,?, 0)");
            //$stmt->bindparam( );

            if ($stmt->execute(array( $first_name, $last_name, $address, $phone, $email,  $password_hash))) {
                // User successfully inserted
                    $tmp =  $this->getDriverIDByEmail($email);
                    $t = $tmp['id'];
                    $this->createDriverImage($t);

                return $tmp;
            } 
        } 
        //return $response;
    }
    public function createDriverImage($id){
        $image = PATCH_TO_IMAGES_DRIVER . $id . EXTENSION_FOR_IMAGES;
         $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("UPDATE drivers set image_url = :id_name  WHERE id = :idd");
        $stmt->bindparam(':id_name', $image);
        $stmt->bindparam(':idd', $id);
        $stmt->execute();


    }
//___________________________________________________________________________________________________
    /**
     * Checking user login
     * @param String $email User login email id
     * @param String $password User login password
     * @return boolean User login status success/fail
     */
    public function checkLoginDriver($email, $password) {
        //$this->conn->query("SET NAMES utf8");
        // fetching user by email
        $stmt = $this->conn->prepare("SELECT id , password_hash FROM drivers WHERE email = :email AND block =0");
        //$stmt->bindparam(":categorias",$categorias);
        $stmt->bindparam(":email", $email);

        $stmt->execute();

        if ($stmt->rowCount()>0) {

            $editRow=$stmt->fetch(PDO::FETCH_ASSOC);
            $password_hash= $editRow["password_hash"];
            if (PassHash::check_password($password_hash, $password)) {
                // User password is correct
                $this->setUserLoginDriver($editRow['id']);

                return TRUE;
            } else {
                // user password is incorrect
                return FALSE;
            }
        } else {
            $stmt->close();

            // user not existed with the email
            return FALSE;
        }
    }
    //___________________________________________________________________________________________________
    /**
     * Creating new task
     * @param String $user_id user id to whom task belongs to
     * @param String $task task text
     */
    public function setUserLoginDriver($user_id) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("UPDATE drivers set api_key = :api_key, status = 1 , date_online = :date_online, working = 0  WHERE id = :idd");
        $dtime = $this->getDatetimeNow();
        $api_key = $this->generateApiKey(); // API key para accesos
        $stmt->bindparam(':api_key', $api_key);
        $stmt->bindparam(':date_online', $dtime);
        $stmt->bindparam(':idd', $user_id);
        $stmt->execute();
    }

    private function getDatetimeNow() {
                $tz_object = new DateTimeZone('America/Caracas');
                //date_default_timezone_set('Brazil/East');

                $datetime = new DateTime();
                $datetime->setTimezone($tz_object);
                return $datetime->format('Y\-m\-d\ h:i:s');
    }



    /**
     * Creating new task
     * @param String $user_id user id to whom task belongs to
     * @param String $task task text
     */
    public function setDriverLogout($email) {
        //$this->conn->query("SET NAMES utf8");
       // $stmt = $this->conn->prepare("UPDATE drivers SET status = 0 , api_key = :apk WHERE  email = :em");
        $stmt = $this->conn->prepare("UPDATE drivers set status = 0 , api_key = '', device_id = '', working =0  WHERE email = '$email' ");
       // $stmt->bindparam(':date_online', 'null');
       // $stmt->bindparam(':email', $email);
       if( $stmt->execute()) {
           return true;
       }else {
           return false;
       }
       // $stmt->close();

    }
    /**
     * Creating new task
     * @param String $user_id user id to whom task belongs to
     * @param String $task task text
     */
    public function setRestaurantLogout($email) {
        $this->conn->query("SET NAMES utf8");
       // $stmt = $this->conn->prepare("UPDATE drivers SET status = 0 , api_key = :apk WHERE  email = :em");
        $stmt = $this->conn->prepare("UPDATE restaurants set status = 0 , api_key = '', device_id = ''  WHERE email = '$email' ");
       // $stmt->bindparam(':date_online', 'null');
       // $stmt->bindparam(':email', $email);
       if( $stmt->execute()) {
           return true;
       }else {
           return false;
       }
       // $stmt->close();

    }
//___________________________________________________________________________________________________
    /**
     * Checking for duplicate user by email address
     * @param String $email email to check in db
     * @return boolean
     */
    private function isDriversExists($email) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT id FROM drivers WHERE email = ?");
        //$stmt->bindparam();
        
       $stmt->execute(array($email));
       //$editRow=$stmt->fetch(PDO::FETCH_ASSOC);
       $num_rows =$stmt->rowCount();
        if($num_rows == 0){
              
           return false;
        }else{
          return true;
        }
       
        
    }
        /**
     * Checking for duplicate user by email address
     * @param String $email email to check in db
     * @return boolean
     */
    private function isManagerExists($email) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT id FROM manager WHERE email = ?");
        //$stmt->bindparam();
        
       $stmt->execute(array($email));
       $editRow=$stmt->fetch(PDO::FETCH_ASSOC); 
       $num_rows =$stmt->rowCount();
        if($num_rows == 0){
              
           return false;
        }else{
          return true;
        }
       
        
    }
        private function isRestaurantExists($email) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT id FROM restaurants WHERE email = ?");
        //$stmt->bindparam();
        
       $stmt->execute(array($email));
       $editRow=$stmt->fetch(PDO::FETCH_ASSOC); 
       $num_rows =$stmt->rowCount();
        if($num_rows == 0){
              
           return false;
        }else{
          return true;
        }
       
        
    }
//___________________________________________________________________________________________________
    /**
     * Fetching user by email
     * @param String $email User email id
     */
    public function getDriverByEmail($email) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT id,first_name,last_name,address,phone,email,api_key,
        image_url, manager, created_At
        FROM drivers WHERE email = :email");

        $stmt->bindparam(":email", $email);
        if ($stmt->execute()) {
            $editRow=$stmt->fetch(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }
    }
    public function getDriverIDByEmail($email) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT id FROM drivers WHERE email = :email");
        $stmt->bindparam(":email", $email);
        if ($stmt->execute()) {
            $editRow=$stmt->fetch(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }
    }
    //___________________________________________________________________________________________________
    /**
     * Fetching user by email
     * @param String $email User email id
     */
    public function getDriverImageById($id) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT image_url 
        FROM drivers WHERE id = :id");

        $stmt->bindparam(":id", $id);
        if ($stmt->execute()) {
            $editRow=$stmt->fetch(PDO::FETCH_ASSOC);
         
            return $editRow;
        } else {
            return NULL;
        }
    }
    public function getDriverManagerArray() {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT id, first_name, last_name, phone
        FROM drivers WHERE manager = 1");
        $stmt->execute();
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);

            return $editRow;

    }

    public function getPhoneRestaurant($id) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT phone FROM restaurat WHERE id = '$id'");
        if ($stmt->execute()) {
            $editRow=$stmt->fetch(PDO::FETCH_ASSOC);

            return $editRow;
        } else {
            return NULL;
        }
    }





    /**
     * Fetching user by email
     * @param String $email User email id
     */
    public function getRestaurantByEmail($email) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT id, name, address, phone, email, api_key, image_url, created_at
            FROM restaurants WHERE email = :email");
        $stmt->bindparam(":email", $email);
        if ($stmt->execute()) {
            $editRow=$stmt->fetch(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }
    }
    public function getRestaurantIDByEmail($email) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT id FROM restaurants WHERE email = :email");
        $stmt->bindparam(":email", $email);
        if ($stmt->execute()) {
            $editRow=$stmt->fetch(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }
    }
//___________________________________________________________________________________________________
    /**
     * Fetching user api key
     * @param String $user_id user id primary key in user table
     */
    public function getApiKeyById($user_id) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT api_key FROM users_api WHERE id = ?");
        //$stmt->bindparam();
        if ($stmt->execute(array($user_id))) {
            // $api_key = $stmt->get_result()->fetch_assoc();
            // TODO
              $editRow=$stmt->fetch(PDO::FETCH_ASSOC);
            $api_key =  $editRow["api_key"];
            return $api_key;
        } else {
            return NULL;
        }
    }
//___________________________________________________________________________________________________
    /**
     * Fetching user id by api key
     * @param String $api_key user api key
     */
    public function getUserId($api_key) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT id FROM users_api WHERE api_key = :api_key");
        $stmt->bindparam(":api_key", $api_key);
        
        if ($stmt->execute()) {
            //$stmt->bind_result($user_id);
            $editRow=$stmt->fetch(PDO::FETCH_ASSOC); 
            $user_id=$editRow["id"];
            //$stmt->fetch();
            // TODO
            // $user_id = $stmt->get_result()->fetch_assoc();
            //$stmt->close();
            return $user_id;
        } else {
            return NULL;
        }
    }
//___________________________________________________________________________________________________
    /**
    *EN UNA NUEVA VERSION SE PUEDE COLOCAR UN TOKEN CON FECHA DE EXPIRACIO
    *ASI CUANDO EXPIRE LA SESION EL USER API INICIE SESION NUEVAMENTE GENERANDO
    * UNA NUEVA APIKEY
     * Validating user api key
     * If the api key is there in db, it is a valid key
     * @param String $api_key user api key
     * @return boolean
     */
    public function isValidApiKey($api_key, $restaurant) {
        $this->conn->query("SET NAMES utf8");

        if($restaurant == 1){
            $sqll= "SELECT id FROM restaurants WHERE api_key = :api_key ";
        }
        if($restaurant == 2){
            $sqll= "SELECT id FROM drivers WHERE api_key = :api_key ";
        }
        if($restaurant == 3){
             $sqll= "SELECT id FROM manager WHERE api_key = :api_key";
        }
        $stmt = $this->conn->prepare($sqll);
        $stmt->bindparam(":api_key", $api_key);
        $stmt->execute();
        $editRow=$stmt->fetch(PDO::FETCH_ASSOC);        
        $num_rows =$stmt->rowCount();
        return $num_rows > 0;
    }


  public function getDeliverysNotificationsLost($id_delivery) {
        $this->conn->query("SET NAMES utf8");
       // $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("SELECT id_delivery FROM push_tmp WHERE id_delivery = '$id_delivery' ");        
        $stmt->execute();
        $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);
        return $editRow;
    }
    public function getDeliverysNotificationsLostV2($id_delivery) {
        $this->conn->query("SET NAMES utf8");
        // $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("SELECT id FROM deliverys WHERE id = '$id_delivery' AND id_driver !='' ");
        $stmt->execute();
        $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);
        return $editRow;
    }


 public function deletePushTmpList($device_id, $id_delivery) {
        $this->conn->query("SET NAMES utf8");
       // $stmt = $this->conn->prepare("UPDATE drivers SET status = 0 , api_key = :apk WHERE  email = :em");
        $stmt = $this->conn->prepare("DELETE FROM push_tmp   WHERE notification_sent = 1  AND id_delivery = '$id_delivery' ");
       // $stmt->bindparam(':date_online', 'null');
       // $stmt->bindparam(':email', $email);
       if( $stmt->execute()) {
           return true;
       }else {
           return false;
       }
       // $stmt->close();

    }
 public function deletePushTmpListAll($id_delivery) {
        $this->conn->query("SET NAMES utf8");
       // $stmt = $this->conn->prepare("UPDATE drivers SET status = 0 , api_key = :apk WHERE  email = :em");
        $stmt = $this->conn->prepare("DELETE FROM push_tmp  WHERE id_delivery = '$id_delivery' ");
       // $stmt->bindparam(':date_online', 'null');
       // $stmt->bindparam(':email', $email);
       if( $stmt->execute()) {
           return true;
       }else {
           return false;
       }
       // $stmt->close();

    }
    public function setUpdateDeliveryDriverAccept($id_delivery, $id_driver, $name_driver, $timer_driver) {
        $this->conn->query("SET NAMES utf8");
        // $stmt = $this->conn->prepare("UPDATE drivers SET status = 0 , api_key = :apk WHERE  email = :em");
        $stmt = $this->conn->prepare("UPDATE deliverys set
                                      id_driver = '$id_driver' ,
                                       name_driver = '$name_driver',
                                       time_driver = '$timer_driver'
                                         WHERE id = '$id_delivery' ");
        if( $stmt->execute()) {

            $stmt = $this->conn->prepare("UPDATE drivers set working = 1
                                         WHERE id = '$id_driver' ");

            if( $stmt->execute()) {


                return true;
            }
        }else {
            return false;
        }
    }

    public function setChooseDelivery($id_delivery, $id_driver, $name_driver, $timer_driver) {
        $this->conn->query("SET NAMES utf8");
        // $stmt = $this->conn->prepare("UPDATE drivers SET status = 0 , api_key = :apk WHERE  email = :em");


        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );


            $stm = $this->conn->prepare("SELECT * FROM deliverys WHERE status = 1
            AND id= '$id_delivery' AND id_driver = 0    ORDER By time");

        $stm->execute();
        $editRow=$stm->fetch();
        if($editRow == false){
            return false;
        }else{
            $stmt = $this->conn->prepare("UPDATE deliverys set
                                          id_driver = '$id_driver' ,
                                           name_driver = '$name_driver',
                                           time_driver = '$timer_driver'
                                             WHERE id = '$id_delivery'
                                             AND id_driver = 0");
            if( $stmt->execute()) {

                $stmt = $this->conn->prepare("UPDATE drivers set working = 1
                                             WHERE id = '$id_driver' ");

                $stmt->execute();
                return true;

            }else {
                return false;
            }

        }
























    }
//___________________________________________________________________________________________________
    /**
     * Generating random Unique MD5 String for user Api key
     */
    private function generateApiKey() {
        return md5(uniqid(rand(), true));
    }
//_________________________________________________________________________________________________

//_________________________________________________________________________________________________

    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function getConfig() {
        $this->conn->query("SET NAMES utf8");
       // $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("SELECT enable_api FROM config ");        
        $stmt->execute();
        $editRow=$stmt->fetch();
        return $editRow;
    }

 //_________________________________________________________________________________________________
    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function getDriver($id) {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("SELECT id, first_name,last_name ,address, phone,email,image_url FROM drivers WHERE id = :id");
        $stmt->bindparam(":id", $id);      
        
        
         if ($stmt->execute()) {  
         $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);          
            return $editRow;
        } else {            
            return NULL;
        }
        return $editRow;
    }
    public function getDriverListBlock() {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("SELECT id, first_name,last_name  FROM drivers WHERE working = 0 AND block =0");

        if ($stmt->execute()) {
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }
        return $editRow;
    }
    public function setBlock($id) {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("UPDATE drivers SET api_key='', status = 0, device_id = '', working = 0, block = 1   WHERE id = '$id'");

        if ($stmt->execute()) {

            return true;
        } else {
            return NULL;
        }

    }
    public function getDriverList($id) {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );

        $sql = "SELECT dd.id, dd.address, dd.note, DATE_FORMAT( dd.time,'%b %d %Y %h:%i %p') As time, dd.time_driver , dd.status ,
                      dd.id_restaurant,dd.id_driver,  rr.name As restaurant,rr.address As restaurant_address, rr.phone As phone_restaurant
                      FROM deliverys As dd, restaurants As rr, drivers As dr
                      WHERE dd.id_restaurant = rr.id AND dd.id_driver = dr.id AND dd.status = 1 AND dd.id_driver = '$id' ";

        $stmt = $this->conn->prepare($sql);
        if ($stmt->execute()) {
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }

    }
    public function getDriverListDetail($id_delivery) {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );

        $sql = "SELECT dd.id, dd.address, dd.note, DATE_FORMAT( dd.time,'%b %d %Y %h:%i %p') As time, dd.time_driver , dd.status ,
                      dd.id_restaurant, dd.id_driver, rr.name As restaurant, rr.address As restaurant_address, rr.phone As phone_restaurant
                      FROM deliverys As dd, restaurants As rr
                      WHERE dd.id_restaurant = rr.id  AND dd.id = '$id_delivery' ";

        $stmt = $this->conn->prepare($sql);
        if ($stmt->execute()) {
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);
            return $editRow[0];
        } else {
            return NULL;
        }

    }
    public function getDriverWorkingList() {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );

        $sql = "SELECT
                      dd.id,
                       dd.first_name,
                       dd.last_name,
                       dd.address,
                       dd.phone,
                       dd.email,
                      DATE_FORMAT( dd.date_online,'%h:%i %p') As date_online,
                      COUNT(deliverys.id_driver) count_delierys
                      FROM drivers AS dd
                      INNER JOIN deliverys
                      ON dd.id = deliverys.id_driver
                      WHERE deliverys.status = 1 and dd.status = 1
                      GROUP BY id_driver
                      ORDER By count_delierys DESC ";

        $stmt = $this->conn->prepare($sql);
        if ($stmt->execute()) {

            $editRow=$stmt->fetchAll();
            $stmt = $this->conn->prepare($sql);
            $stmt->execute();
            $editRowFinal=$stmt->fetchAll(PDO::FETCH_ASSOC);



            $sql2 = "SELECT
                      dd.id,
                       dd.first_name,
                       dd.last_name,
                       dd.address,
                       dd.phone,
                       dd.email,
                      DATE_FORMAT( dd.date_online,'%h:%i %p') As date_online
                      FROM drivers AS dd
                      where dd.status=1";

            $stmt2 = $this->conn->prepare($sql2);
            $stmt2->execute();
                $editRow2=$stmt2->fetchAll();



            $countSQL = count($editRow);
           $countSQL2 = count($editRow2);


            $response = array();

            for($i = 0; $i < $countSQL2; $i++) {

                $provado = $editRow2[$i][0];

                for($ii = 0; $ii < $countSQL; $ii++) {
                    $tmpPrueba = $editRow[$ii][0];


                    if($tmpPrueba != $provado){
                        $rsOk = true;
                    }else{
                        $rsOk = false;
                        break;
                    }
                }
                if($rsOk){
                    $tmp = array("id"=> $editRow2[$i][0],
                        "first_name"=> $editRow2[$i][1],
                        "last_name"=> $editRow2[$i][2],
                        "address"=> $editRow2[$i][3],
                        "phone"=> $editRow2[$i][4],
                        "email"=> $editRow2[$i][5],
                        "date_online"=> $editRow2[$i][6],
                        "count_delierys"=>0  );
                    array_push($editRowFinal, $tmp);
                }

            }

            $duplicates = array_map("unserialize", array_unique(array_map("serialize", $editRow)));

         /*   for($i = 0; $i < count($editRow); $i++) {


                for($ii = 0; $ii < count($editRow2); $ii++) {
                    if( $editRow[$i]->id !=  $editRow2[$ii]->id){


                    }
                }







                if( $editRow[$i]->id !=  $editRow2[$i]->id){
                    $countSQL1 = $countSQL1  +1;
                    $editRow[$countSQL1]->id = $editRow[$i]->id;
                    $editRow[$countSQL1]->first_name = $editRow[$i]->first_name;
                    $editRow[$countSQL1]->last_name = $editRow[$i]->last_name;
                    $editRow[$countSQL1]->address = $editRow[$i]->address;
                    $editRow[$countSQL1]->phone = $editRow[$i]->phone;
                    $editRow[$countSQL1]->email = $editRow[$i]->email;
                    $editRow[$countSQL1]->date_online = $editRow[$i]->date_online;
                }

            }*/




            return $editRowFinal;
        } else {
            return NULL;
        }

    }


      /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function getRestaurant($id) {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("SELECT id, name, address, phone, email,image_url FROM restaurants WHERE id = :id");
        $stmt->bindparam(":id", $id);      
        
        
         if ($stmt->execute()) {  
         $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);          
            return $editRow;
        } else {            
            return NULL;
        }
        return $editRow;
    }
    public function getRestaurantList() {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("SELECT id, name FROM restaurants ");



        if ($stmt->execute()) {
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);

          
            return $editRow;
        } else {
            return NULL;
        }
        return $editRow;
    }

    public function getRestaurantList2() {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("SELECT id, name FROM restaurants ");



        if ($stmt->execute()) {
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);

            $datos = array();
            foreach ($editRow as $key => $value) {
                $sql="select time from deliverys where id_restaurant=".$value['id']." order by id desc LIMIT 1";
                $stmt2 = $this->conn->prepare($sql);
                $deliveries = false;
                if($stmt2->execute()){
                    $editRow2=$stmt2->fetchAll(PDO::FETCH_ASSOC);

                    if(count($editRow20)>0){
                        $time=new DateTime($editRow2[0]['time']);
                        $now=new DateTime("now");
                        $diff=$now->diff($time);
                        $hours=$diff->h;
                        if($hours>=2){
                            $deliveries=false;
                        }else{
                            $deliveries=true;
                        }
                    }else{
                        $deliveries=false;
                    }
                }

                $datos[] = array('id' => $value['id'], 'name'=> $value['name'], 'deliveriesInTwoHours' => $deliveries); 
            }
            
            return $datos;
        } else {
            return NULL;
        }
        return $editRow;
    }




    public function getDelivery($id) {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );

        if(isset($id)){
            $stmt = $this->conn->prepare("SELECT * FROM deliverys WHERE id = :id");
            $stmt->bindparam(":id", $id);
        }else{
            $stmt = $this->conn->prepare("SELECT * FROM deliverys WHERE deliverys.status = 1      ORDER By time");
        }
    }
    public function getDeliveryDetail($id) {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );

      $sql="SELECT dd.id, dd.address As delivery_address, dd.note, DATE_FORMAT( dd.time,'%b %d %Y %h:%i %p') As time, dd.time_driver,
                   dr.id As id_driver, dr.first_name, dr.last_name, dr.address As driver_address, dr.phone, dr.image_url
                      FROM deliverys As dd, drivers As dr
                      WHERE dd.id_driver = dr.id AND dd.id = '$id' AND dd.id_driver != '' AND dd.name_driver != ''";
            $stmt = $this->conn->prepare($sql);
            $stmt->execute();
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);
            return $editRow[0];

    }

    public function getNoticeDelivery($id_delivery) {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );

        $sql = "SELECT deliverys.id As id_delivery,
                  deliverys.address As delivery_address,
                  deliverys.time,
                   restaurants.id As id_restaurant,
                    restaurants.name As restaurant_name,
                     restaurants.address As restaurant_address,
                      restaurants.phone, restaurants.email
                      FROM deliverys, restaurants
                      WHERE restaurants.id = deliverys.id_restaurant
                      AND deliverys.id = '$id_delivery' ";

        $stmt = $this->conn->prepare($sql);
        if ($stmt->execute()) {
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);
            return $editRow[0];
        } else {
            return NULL;
        }
        return $editRow;
    }

    public function getDeliverys($id_restaurant){
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $sql = 'SELECT id,
                      id_restaurant,
                      address As delivery_address,
                      note,
                      DATE_FORMAT(time,\'%b %d %Y %h:%i %p\') As time,
                      id_driver,
                      name_driver,
                      time_driver,
                      status FROM deliverys WHERE status = 1 AND id_restaurant = :id'
                     . ' ORDER By time DESC';

            $stmt = $this->conn->prepare($sql);
            $stmt->bindparam(":id", $id_restaurant);        
        
         if ($stmt->execute()) {  
         $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);          
            return $editRow;
        } else {            
            return NULL;
        }
        return $editRow;
    }

    public function getDeliverysHistory($id_restaurant) {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $sql = 'SELECT id,
                      id_restaurant,
                      address As delivery_address,
                      note,
                      DATE_FORMAT(time,\'%b %d %Y %h:%i %p\') As time,
                      id_driver,
                      name_driver,
                      time_driver,
                      status FROM deliverys WHERE status = 0 AND id_restaurant = :id'
            . ' ORDER By time DESC LIMIT ' .LIMIT_HISTORY ;

        $stmt = $this->conn->prepare($sql);
        $stmt->bindparam(":id", $id_restaurant);

        if ($stmt->execute()) {
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }
        return $editRow;
    }
    public function getDeliverysHistoryDriver($id_driver) {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );

        $stmt = $this->conn->prepare("SELECT * FROM deliverys WHERE status = 0 AND id_driver = :id
                                          ORDER By time DESC LIMIT " . LIMIT_HISTORY);
        $stmt->bindparam(":id", $id_driver);

        if ($stmt->execute()) {
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }
        return $editRow;
    }



    public function getDeliverysFree() {
        //$this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $sql="SELECT
        dd.id, dd.address, dd.note, DATE_FORMAT( dd.time,'%b %d %Y %h:%i %p') As time,  dd.id_restaurant, dd.status,
                    rr.name As restaurant,rr.address As restaurant_address, rr.phone As phone_restaurant
                      FROM deliverys As dd, restaurants As rr
                       WHERE dd.status = 1 AND dd.id_driver = 0  AND dd.id_restaurant = rr.id ORDER BY dd.time";
        $stmt = $this->conn->prepare($sql);

        if ($stmt->execute()) {
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }
        return $editRow;
    }

    public function getDeliverysFreeCount() {
        //$this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $sql="SELECT Count(*) As total  FROM deliverys As dd
                       WHERE dd.status = 1 AND dd.id_driver = 0 ";
        $stmt = $this->conn->prepare($sql);

        if ($stmt->execute()) {
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }
        return $editRow;
    }

    public function deleteDeliery($id_delivery) {
        $this->conn->query("SET NAMES utf8");
        // $stmt = $this->conn->prepare("UPDATE drivers SET status = 0 , api_key = :apk WHERE  email = :em");
        $stmt = $this->conn->prepare("DELETE FROM deliverys   WHERE id = '$id_delivery'  ");
        if( $stmt->execute()) {
            return true;
        }else {
            return false;
        }
        // $stmt->close();

    }
      public function getDeliveryDriver($id) {
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );

        if(isset($id)){
            $stmt = $this->conn->prepare("SELECT * FROM deliverys, drivers WHERE id = :id AND  
                deliverys.id_driver = drivers.id ");
            $stmt->bindparam(":id", $id);
        }else{
            $stmt = $this->conn->prepare("SELECT * FROM deliverys, drivers WHERE deliverys.status = 1  AND  
                deliverys.id_driver = drivers.id  ORDER By time");
        }
        
         if ($stmt->execute()) {  
         $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);          
            return $editRow;
        } else {            
            return NULL;
        }
        return $editRow;
    }
    //_________________________________________________________________________________________________
    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function updateDriver($id, $first_name, $last_name,  $address, $phone, $email, $imageBool ){
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $sql = "";
        $im = "";
        if($imageBool){
            $sql = "UPDATE drivers SET  first_name = :first_name ,
                                                            last_name = :last_name ,
                                                          address = :address ,
                                                          phone = :phone ,
                                                          email = :email,
                                                          image_url = :image_url
                                                          WHERE id = :id";
            $im=PATCH_TO_IMAGES_DRIVER.  $id . EXTENSION_FOR_IMAGES;
        }else{
            $sql = "UPDATE drivers SET  first_name = :first_name ,
                                                            last_name = :last_name ,
                                                          address = :address ,
                                                          phone = :phone ,
                                                          email = :email
                                                          WHERE id = :id";
        }



        $stmt = $this->conn->prepare($sql);

        $stmt->bindparam(":first_name", $first_name);
        $stmt->bindparam(":last_name", $last_name);
        $stmt->bindparam(":address", $address);
        $stmt->bindparam(":phone", $phone);
        $stmt->bindparam(":email", $email);
        $stmt->bindparam(":image_url",$im );
        $stmt->bindparam(":id", $id);
        $stmt->execute();

        return  $this->getDriver($id);
    }



 /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function updateRestaurant($id, $name,  $address, $phone, $email ){
        $image = PATCH_TO_IMAGES_RESTAURANT . $id . EXTENSION_FOR_IMAGES;
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("UPDATE restaurants SET  name = :name , 
                                                          address = :address , 
                                                          phone = :phone ,
                                                          email = :email,
                                                          image_url =:image
                                                          WHERE id = :id");

        $stmt->bindparam(":name", $name);
        $stmt->bindparam(":address", $address);
        $stmt->bindparam(":phone", $phone);
        $stmt->bindparam(":email", $email);
        $stmt->bindparam(":image", $image);
        $stmt->bindparam(":id", $id);
        $stmt->execute();

        return  $this->getRestaurant($id);
    }

 /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function updateDelivery($id, $address, $note, $id_driver, $time_driver){
        $this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("UPDATE deliverys SET  address = :address , 
                                                          note = :note , 
                                                          id_driver = :id_driver ,
                                                          time_driver = :time_driver                                                
                                                          WHERE id = :id");

        $stmt->bindparam(":address", $address);
        $stmt->bindparam(":note", $note);
        $stmt->bindparam(":id_driver", $id_driver);
        $stmt->bindparam(":time_driver", $time_driver);
        $stmt->bindparam(":id", $id);
        $stmt->execute();

        return  $this->getDelivery($id);
    }


    public function FinishDelivery($id,$id_driver){
        //$this->conn->query("SET NAMES utf8");
        //$this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("UPDATE deliverys SET  status = 0
                                                          WHERE id = '$id' ");

        $stmt->execute();
        $this->FinishDeliveryDriver($id_driver);

        return  true;
    }
    private function FinishDeliveryDriver($id_driver){
        //$this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("UPDATE drivers SET  working = 0
                                                          WHERE id = :id");
        $stmt->bindparam(":id", $id_driver);
        $stmt->execute();
    }

    public function updateDeliveryTime($data){

        //$this->conn->query("SET NAMES utf8");
        $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
        $stmt = $this->conn->prepare("UPDATE deliverys SET  time_driver = :time_driver
                                                          WHERE id = :id ");

        for($i = 0; $i < count($data); $i++) {
            $tmpId = $data[$i][0];
            $tmpTime = $data[$i][1];
            $stmt->bindValue(':time_driver', $tmpTime, PDO::PARAM_STR);
            $stmt->bindValue(':id', $tmpId, PDO::PARAM_STR);
            $stmt->execute();
        }

        return  true;
    }





//___________________________________________________________________________________________________
// RESTAURANT FUNCTIONS
//___________________________________________________________________________________________________

   /**
     * Creacion de un usuario
     * @param String $name User full name
     * @param String $email User login email id
     * @param String $password User login password
     */
    public function createRestaurant($name, $address, $phone, $email, $password ) {
        require_once 'PassHash.php';
        $response = array();

        // Verifica que usuario no exista ya en db
        if (!$this->isRestaurantExists($email)) {
            
            $password_hash = PassHash::hash($password); //password hash            

            $this->conn->query("SET NAMES utf8");
            $stmt = $this->conn->prepare("INSERT INTO `restaurants`
                (`name`, `address`, `phone`, `email`, `password_hash`,  `status`) values(?, ?, ?,?,?, 0)");
            //$stmt->bindparam( );

            if ($stmt->execute(array( $name, $address, $phone, $email,  $password_hash))) {
                $tmp = $this->getRestaurantIDByEmail($email);
                $t = $tmp["id"];
                 $this->createRestaurantImage($t);

                return $tmp;
            } 
        } 
        //return $response;
    }
        public function createRestaurantImage($id){

        $image =    PATCH_TO_IMAGES_RESTAURANT . EXTENSION_FOR_IMAGES;
         $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("UPDATE restaurants set image_url = :id_name  WHERE id = :idd");
        $stmt->bindparam(':id_name', $image);
        $stmt->bindparam(':idd', $id);
        $stmt->execute();


    }
  /**
     * Checking user login
     * @param String $email User login email id
     * @param String $password User login password
     * @return boolean User login status success/fail
     */
    public function checkLoginRestaurant($email, $password) {
        $this->conn->query("SET NAMES utf8");
        // fetching user by email
        $stmt = $this->conn->prepare("SELECT id , password_hash FROM restaurants WHERE email = :email");
        //$stmt->bindparam(":categorias",$categorias);
        $stmt->bindparam(":email", $email);

        $stmt->execute();

        if ($stmt->rowCount()>0) {

            $editRow=$stmt->fetch(PDO::FETCH_ASSOC);
            $password_hash= $editRow["password_hash"];
            if (PassHash::check_password($password_hash, $password)) {
                // User password is correct
                $this->setUserLoginRestaurant($editRow['id']);

                return TRUE;
            } else {
                // user password is incorrect
                return FALSE;
            }
        } else {
            // user not existed with the email
            return FALSE;
        }
    }

  /**
     * Creating new task
     * @param String $user_id user id to whom task belongs to
     * @param String $task task text
     */
    public function setUserLoginRestaurant($user_id) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("UPDATE restaurants set api_key = :api_key, status = 1   WHERE id = :idd");
       // $dtime = $this->getDatetimeNow();
        $api_key = $this->generateApiKey(); // API key para accesos
        //$stmt->bindparam(array(1, 1) );
        //$stmt->bindparam(':sta', 1, PDO::PARAM_INT);
        $stmt->bindparam(':api_key', $api_key);
       // $stmt->bindparam(':date_online', $dtime);
        $stmt->bindparam(':idd', $user_id);
        //$stmt->bindparam(":id", $user_id);
        //$stmt->bindparam(":telefonos", $telefonos);
        //$stmt->bindparam(":masinfo", $masinfo);
        //$stmt->execute(array(1, $user_id));
        //$stmt->close();
        $stmt->execute();
       // $stmt->close();

    }




   public function createDelivery($address, $note, $id_restaurant) {
       
        $response = array();                 

            $this->conn->query("SET NAMES utf8");
            $stmt = $this->conn->prepare("INSERT INTO `deliverys` 
                (`id_restaurant`,`address`, `note`) values(?, ?, ?)");
            //$stmt->bindparam( );

           /* $myfile = fopen("log.txt", "w");
            $txt = "INSERT INTO `deliverys` 
                (`id_restaurant`,`address`, `note`) values(".$id_restaurant.",'".$address."','".$note."')";
            fwrite($myfile, $txt);
            fclose($myfile);*/

            if ($stmt->execute(array( $id_restaurant, $address, $note))) {
                // User successfully inserted
                $stmt = $this->conn->prepare("SELECT LAST_INSERT_ID() as last_id FROM deliverys LIMIT 1");
                $stmt->execute();
                $last_id = $stmt->fetchAll();

                $last_id = $last_id[0]['last_id'];

                return $last_id;
            } else {
                // Failed to create user
                return USER_CREATE_FAILED;
            }
      

        //return $response;
    }





















 /**
     * Creacion de un usuario
     * @param String $name User full name
     * @param String $email User login email id
     * @param String $password User login password
     */
    public function createManager($email, $password ) {
        require_once 'PassHash.php';
        $response = array();

        // Verifica que usuario no exista ya en db
        if (!$this->isManagerExists($email)) {
            
            $password_hash = PassHash::hash($password); //password hash            

$this->conn->query("SET NAMES utf8");
            $stmt = $this->conn->prepare("INSERT INTO `manager` ( `email`, `password_hash`) values(?, ?)");
            //$stmt->bindparam( );

            if ($stmt->execute(array($email,  $password_hash))) {
                // User successfully inserted
                return USER_CREATED_SUCCESS;
            } else {
                // Failed to create user
                return USER_CREATE_FAILED;
            }
        } else {
            // User with same email already existed in the db
            return USER_ALREADY_EXISTED;
        }

        //return $response;
    }

 public function checkLoginManager($email, $password) {
        $this->conn->query("SET NAMES utf8");
        // fetching user by email
        $stmt = $this->conn->prepare("SELECT id , password_hash FROM manager WHERE email = :email");
        //$stmt->bindparam(":categorias",$categorias);
        $stmt->bindparam(":email", $email);

        $stmt->execute();
        if ($stmt->rowCount()>0) {

            $editRow=$stmt->fetch(PDO::FETCH_ASSOC);
            $password_hash= $editRow["password_hash"];
            if (PassHash::check_password($password_hash, $password)) {
                // User password is correct
                $this->setUserLoginManager($editRow['id']);

                return TRUE;
            } else {
                // user password is incorrect
                return FALSE;
            }
        } else {
            // user not existed with the email
            return FALSE;
        }
    }

    public function setUserLoginManager($user_id) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("UPDATE manager set api_key = :api_key  WHERE id = :idd");
        $dtime = $this->getDatetimeNow();
        $api_key = $this->generateApiKey(); // API key para accesos
        $stmt->bindparam(':api_key', $api_key);
        $stmt->bindparam(':idd', $user_id);
        $stmt->execute();
    }

    public function setManagerLogout($email) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("UPDATE manager set api_key = ''  WHERE email = '$email' ");
       if( $stmt->execute()) {
           return true;
       }else {
           return false;
       }

    }
    public function getRestorePassword($email) {
        $this->conn->query("SET NAMES utf8");
        $api_key = $this->generateApiKey();
        $stmt = $this->conn->prepare("UPDATE drivers set api_key = '$api_key', status = 0, working = 0  WHERE email = '$email'");
        $stmt->execute();
        return $api_key;

    }
    public function getRestorePasswordRestaurat($email) {
        $this->conn->query("SET NAMES utf8");
        $api_key = $this->generateApiKey();
        $stmt = $this->conn->prepare("UPDATE restaurants set api_key = '$api_key', status = 0 WHERE email = '$email'");
        $stmt->execute();
        return $api_key;

    }
    public function getRestorePasswordNew($email, $password, $token) {
        $this->conn->query("SET NAMES utf8");
        $api_key = $this->generateApiKey();
        $password_hash = PassHash::hash($password); //password hash
        $stmt = $this->conn->prepare("UPDATE drivers set
                                      password_hash = '$password_hash', api_key = '$api_key', device_id =''
                                      WHERE email = '$email' AND api_key = '$token'");
        $stmt->execute();
        return true;

    }
    public function getRestorePasswordNewRestaurant($email, $password, $token) {
        $this->conn->query("SET NAMES utf8");
        $api_key = $this->generateApiKey();
        $password_hash = PassHash::hash($password); //password hash
        $stmt = $this->conn->prepare("UPDATE restaurants set
                                      password_hash = '$password_hash', api_key = '$api_key', device_id =''
                                      WHERE email = '$email' AND api_key = '$token'");
        $stmt->execute();
        return true;

    }
    public function getManagerByEmail($email) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("SELECT id, email, api_key, created_At 
        FROM manager WHERE email = :email");

        $stmt->bindparam(":email", $email);
        if ($stmt->execute()) {
            $editRow=$stmt->fetch(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }
    }

    public function setOffLine($id) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("UPDATE drivers SET status = 0, device_id = '' WHERE id = '$id'");
        if ($stmt->execute()) {
            $editRow=$stmt->fetch(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }
    }

    public function setOnLine($id, $device_id) {
        $this->conn->query("SET NAMES utf8");
        $stmt = $this->conn->prepare("UPDATE drivers SET status = 1, device_id = '$device_id', date_online = NOW() WHERE id = '$id'");
        if ($stmt->execute()) {
            $editRow=$stmt->fetch(PDO::FETCH_ASSOC);
            return $editRow;
        } else {
            return NULL;
        }
    }






























//_________________________________________________________________________________________________
    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function getSearch($categorias) {
        $this->conn->query("SET NAMES utf8");
       // $this->conn->setAttribute( PDO::ATTR_EMULATE_PREPARES, false );
       // $this->conn->query("SET NAMES utf8");
        //$this->conn>query("SET CHARACTER_SET utf8_polish_ci");
        $stmt = $this->conn->prepare("SELECT * FROM main WHERE 
              categorias  LIKE ? 
            OR categorias LIKE ?  
            OR categorias LIKE ? 
            OR categorias LIKE ?");  
        
        $var1= "%" . $categorias . "%";
        $var2= "%" . strtolower($categorias) . "%";
        $var3= "%" . ucfirst($categorias) . "%";
        $var4= "%" . strtoupper($categorias) . "%";
        $params = array($var1, $var2, $var3, $var4 );
        
        
        
         if ($stmt->execute($params)){  
            $editRow=$stmt->fetchAll(PDO::FETCH_ASSOC);   
            return $editRow;
        } else {            
            return NULL;
        }
        return $editRow;
    }
  

}

?>
