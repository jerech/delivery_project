<?php

/*
*   clase para manejar la base de datos 
*   creada el 13 de febrero del 2016
*   implemetada en la api rest full de sangolqui
*   by Paul Osinga
*/
class Db {

    private $conn;

    function __construct() {      }

    /**
     * Estabiliza la conneccion
     * @return manejador de base de dtos PDO
     */
    function connect() {
        //include_once dirname(__FILE__) . '/Config.php';
        include_once dirname(__FILE__) . '/configuration.php';
            try
            {
                $this->conn = new PDO("mysql:host=" .  DATABASE_HOST .";dbname=" . DATABASE_NAME ,
                    DATABASE_USERNAME, DATABASE_PASSWORD);  
                $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);   
                
            }
            catch(PDOException $e)
            {
                echo $e->getMessage();
            }
        return $this->conn;
    }

}

?>
