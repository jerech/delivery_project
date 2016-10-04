<?php

require_once '../include/HandlerDb.php';
require_once '../include/PassHash.php';
require_once '../include/ImageResizer.php';
require_once '../include/gcm.php';
require '.././libs/Slim/Slim.php';

\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();
$app->config(array(
    'debug' => true,
    'templates.path' => 'templates'
));

require 'functions.php';
require 'driver.php';
require 'restaurant.php';
require 'deliverys.php';
require 'utils.php';


$app->run();

?>
