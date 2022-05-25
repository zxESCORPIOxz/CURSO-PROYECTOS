<?php

include 'conexion.php';

$json=array();
    if(isset($_GET["nCorreo"])){
        $nCorreo=$_GET['nCorreo'];

        $consulta="CALL CON_NOMBRE('{$nCorreo}')";
        $resultado=mysqli_query($conexion,$consulta);

        if($fila = mysqli_fetch_array($resultado)){
            $json['usuario']="{$fila['apellido']} {$fila['nombre']}";
        }
        mysqli_close($conexion);
        echo json_encode($json);
    }
?>