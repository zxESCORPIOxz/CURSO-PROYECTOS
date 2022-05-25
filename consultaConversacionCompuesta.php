<?php
include 'conexion.php';

$json=array();
    if(isset($_GET["nCorreo"]) 
    && isset($_GET["nidPublicacionUsuario"])
    && isset($_GET["nReceptor"])){
        $nCorreo=$_GET['nCorreo'];
        $nidPublicacionUsuario=$_GET['nidPublicacionUsuario'];
        $nReceptor=$_GET['nReceptor'];
        $consulta="CALL CON_CONVERSACION_COMPRADOR('{$nidPublicacionUsuario}','{$nReceptor}','{$nCorreo}')";
        $resultado=mysqli_query($conexion,$consulta);
        if($fila = mysqli_fetch_array($resultado)){
            $json['Consulta']= $fila;
        } 
        mysqli_close($conexion);
        echo json_encode($json);
    }else{
        die("Fallo la conexion");
    }
?>