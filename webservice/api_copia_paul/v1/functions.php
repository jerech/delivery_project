<?php 

// User id from db - Global Variable
// $user_id = NULL; GLOBAL
//___________________________________________________________________________________________________
if( !function_exists('apache_request_headers') ) {
///
    function apache_request_headers() {
        $arh = array();
        $rx_http = '/\AHTTP_/';
        foreach($_SERVER as $key => $val) {
            if( preg_match($rx_http, $key) ) {
                $arh_key = preg_replace($rx_http, '', $key);
                $rx_matches = array();
                // do some nasty string manipulations to restore the original letter case
                // this should work in most cases
                $rx_matches = explode('_', $arh_key);
                if( count($rx_matches) > 0 and strlen($arh_key) > 2 ) {
                    foreach($rx_matches as $ak_key => $ak_val) $rx_matches[$ak_key] = ucfirst($ak_val);
                    $arh_key = implode('-', $rx_matches);
                }
                $arh[$arh_key] = $val;
            }
        }
        return( $arh );
    }
///
}

/**
 * Funcion intermedia pra validar usuario
 * se envia una llave secreta y el 
 * cliente  reenviara cada coltulta con un HEeader Autenticae
 * con el valor de la llave para poder recibir los datos Rest
 */
function authenticate(\Slim\Route $route)  {
    // Getting request headers
    $headers = apache_request_headers();

    $response = array();
    $app = \Slim\Slim::getInstance();
    //$headers = Array("Authorization"=>apache_request_headers()["Authorization"]);



    // Verifying Authorization Header
    if (isset($headers['Authorization']) ) {
        $db = new HandlerDb();

        // get the api key
        $api_key = $headers['Authorization'];
        // validating api key
        if (!$db->isValidApiKey($api_key, 2 )) {
            // api key is not present in users table
            $response["error"] = true;
            $response["message"] = "Access Denied. Invalid Api key";
            echoRespnse(401, $response);
            $app->stop();
            } 
    } else {
        // api key is missing in header
        $response["error"] = true;
        $response["message"] = "Api key is misssing";
        echoRespnse(400, $response);
        $app->stop();
    }

}
function authenticateRestaurant(\Slim\Route $route) {

    $headers = apache_request_headers();
    $response = array();
    $app = \Slim\Slim::getInstance();

    //$headers = Array("Authorization"=>apache_request_headers()["Authorization"]);

    // Verifying Authorization Header
    if (isset($headers['Authorization'])) {
        $db = new HandlerDb();

        // get the api key
        $api_key = $headers['Authorization'];
        // validating api key
        if (!$db->isValidApiKey($api_key, 1)) {
            // api key is not present in users table
            $response["error"] = true;
            $response["message"] = "Access Denied. Invalid Api key";
            echoRespnse(401, $response);
            $app->stop();
            } 
    } else {
        // api key is missing in header
        $response["error"] = true;
        $response["message"] = "Api key is misssing";
        echoRespnse(400, $response);
        $app->stop();
    }

}
function authenticateManager(\Slim\Route $route) {
    // Getting request headers
    $headers = apache_request_headers();
    $response = array();
    $app = \Slim\Slim::getInstance();

    //$headers = Array("Authorization"=>apache_request_headers()["Authorization"]);

    // Verifying Authorization Header
    if (isset($headers['Authorization']) ) {
        $db = new HandlerDb();

        // get the api key
        $api_key = $headers['Authorization'];
        // validating api key
        if (!$db->isValidApiKey($api_key, 3)) {
            // api key is not present in users table
            $response["error"] = true;
            $response["message"] = "Access Denied. Invalid Api key";
            echoRespnse(401, $response);
            $app->stop();
            } 
    } else {
        // api key is missing in header
        $response["error"] = true;
        $response["message"] = "Api key is misssing";
        echoRespnse(400, $response);
        $app->stop();
    }
}

function save_base64_image($base64_image_string, $output_file_without_extentnion ) {

        $extension='jpg';
        $output_file = dirname(__FILE__) .  $output_file_without_extentnion . '.' .$extension;
   // }
    if (file_exists($output_file)) {
        unlink($output_file);
    }

    file_put_contents( $output_file, base64_decode($base64_image_string) );

    return $output_file;
}

function getRemoteFile($url, $timeout = 10) {
  $ch = curl_init();
  curl_setopt ($ch, CURLOPT_URL, $url);
  curl_setopt ($ch, CURLOPT_RETURNTRANSFER, 1);
  curl_setopt ($ch, CURLOPT_CONNECTTIMEOUT, $timeout);
  $file_contents = curl_exec($ch);
  curl_close($ch);
  return ($file_contents) ? $file_contents : FALSE;
}





//___________________________________________________________________________________________________
/**
 * Verifying required params posted or not
 */
function verifyRequiredParams($required_fields) {
    $error = false;
    $error_fields = "";
    $request_params = array();
    $request_params = $_REQUEST;
    // Handling PUT request params
    if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
        $app = \Slim\Slim::getInstance();
        parse_str($app->request()->getBody(), $request_params);
    }
    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }

    if ($error) {
        // Required field(s) are missing or empty
        // echo error json and stop the app
        $response = array();
        $app = \Slim\Slim::getInstance();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echoRespnse(400, $response);
        $app->stop();
    }
}
//___________________________________________________________________________________________________
/**
 * Validating email address
 */
function validateEmail($email) {
    $app = \Slim\Slim::getInstance();
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $response["error"] = true;
        $response["message"] = 'Email address is not valid';
        echoRespnse(400, $response);
        $app->stop();
    }
}
//___________________________________________________________________________________________________
/**
 * Respuesta en Json
 * @param String $status_code Http response code
 * @param Int $response Json response
 */
function echoRespnse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);

    // setting response content type to json
    $app->contentType('application/json');

    echo json_encode($response ,JSON_PRETTY_PRINT);
     //echo $response;
}

function sendEmailResetPassDriver($email, $token){
    $to      = $email;
    $subject = 'Reset Password';
    $message = "<h4>Hi.</h4><p>
                It has requested a password change to accomplish it follows the link below.
                 If you do not remember having made this request simply ignore this mail.</p>
<b><a href=http://deliveryeveryday.com/api/v1/DriverRestore?q=$token&e=$email>reset Password</a></b>";
// To send HTML mail, the Content-type header must be set
    $headers  = 'MIME-Version: 1.0' . "\r\n";
    $headers .= 'Content-type: text/html; charset=UTF-8' . "\r\n";
    $headers .= 'From: info@deliveryeveryday.com' . "\r\n" .
        'Reply-To: deliveryeveryday@gmail.com' . "\r\n" .
        'X-Mailer: PHP/' . phpversion();


    $send = mail($to, $subject, $message, $headers);

}
function sendEmailResetPassRestaurant($email, $token){
    $to      = $email;
    $subject = 'Reset Password';
    $message = "<h4>Hi.</h4><p>
                It has requested a password change to accomplish it follows the link below.
                 If you do not remember having made this request simply ignore this mail.</p>
<b><a href=http://deliveryeveryday.com/api/v1/RestaurantRestore?q=$token&e=$email>reset Password</a></b>";
// To send HTML mail, the Content-type header must be set
    $headers  = 'MIME-Version: 1.0' . "\r\n";
    $headers .= 'Content-type: text/html; charset=UTF-8' . "\r\n";
    $headers .= 'From: info@deliveryeveryday.com' . "\r\n" .
        'Reply-To: deliveryeveryday@gmail.com' . "\r\n" .
        'X-Mailer: PHP/' . phpversion();


    $send = mail($to, $subject, $message, $headers);

}