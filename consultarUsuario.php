<?php

include 'conexion.php';

$json=array();
    if(isset($_GET["idUsuario"])){
        $idUsuario=$_GET['idUsuario'];

        $consulta="CALL CON_USUARIO('{$idUsuario}')";
        $resultado=mysqli_query($conexion,$consulta);

        if($fila = mysqli_fetch_array($resultado)){
            $json['usuario']=$fila;
        }
        mysqli_close($conexion);
        echo json_encode($json);
    }
?>