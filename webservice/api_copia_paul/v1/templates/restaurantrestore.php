<!DOCTYPE html>
<html lang="es">
<head>
    <meta name="author" content="denker">
    <title> Restablecer contraseña </title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>

<body>
<div class="container" role="main">
    <div class="col-md-4"></div>
    <div class="col-md-4">
        <form action="http://deliveryeveryday.com/api/v1/RestaurantNewPass" method="post">
            <div class="panel panel-default">
                <div class="panel-heading"> Reset Password </div>
                <div class="panel-body">
                    <p></p>
                    <div class="form-group">
                        <label for="password"> password </label>
                        <input type="password"  class="form-control" name="password" required ">
                    </div>
                    <div class="form-group">
                        <label for="password2"> repeat password </label>
                        <input type="password"  class="form-control" name="password2" required>
                    </div>
                    <input type="hidden" name="token" value="<?php echo htmlspecialchars($_GET["q"])  ?>">
                    <input type="hidden" name="email" value="<?php echo htmlspecialchars($_GET["e"]) ?>">
                    <div class="form-group">
                        <input type="submit" class="btn btn-primary" value="Reset pass" >
                    </div>
                </div>
            </div>
        </form>
    </div>
    <div class="col-md-4"></div>
</div> <!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</body>
</html>
<?php
/**
 * Created by PhpStorm.
 * User: paulpwo
 * Date: 7/8/16
 * Time: 15:17
 */