<?php
include 'conexion.php';

$json=array();
    if(isset($_GET["nCorreo"]) 
    && isset($_GET["nVariedad"])){
        $nCorreo=$_GET['nCorreo'];
        $nVariedad=$_GET['nVariedad'];
        if($nVariedad=="Seleccione la variedad para filtrar"){
            $consulta="CALL CON_PUBLICACION_SIMPLE('{$nCorreo}')";
        }else{
            $consulta="CALL CON_PUBLICACION_COMPUESTA('{$nCorreo}','{$nVariedad}')";
        }
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