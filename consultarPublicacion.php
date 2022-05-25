<?php
include 'conexion.php';

$json=array();
    if(isset($_GET["nCorreo"])){
        $nCorreo=$_GET['nCorreo'];
        $consulta="CALL CON_PUBLICACIONES('{$nCorreo}')";
        $resultado=mysqli_query($conexion,$consulta);
        while($fila = mysqli_fetch_array($resultado)){
            $json['Consulta'][]= $fila;
        }
        mysqli_close($conexion);
        echo json_encode($json);
    }else{
        die("Fallo la conexion");
    }
?>