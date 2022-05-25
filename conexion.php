<?php
$hostname_localhost="localhost";
$database_localhost="potatosmarketdb";
$username_localhost="root";
$password_localhost="";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
if(mysqli_connect_errno() ){
    echo "Ocurrio un problema con el servidor" . mysqli_connect_errno();
}
?>