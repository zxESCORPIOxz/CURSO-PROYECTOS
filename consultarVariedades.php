<?php
include 'conexion.php';

$json=array();

    $consulta="CALL CON_VARIEDADES()";
    $resultado = mysqli_query($conexion,$consulta);

    while($fila = mysqli_fetch_array($resultado)){
        $json['variedades'][]=$fila;
    }
    mysqli_close($conexion);
    echo json_encode($json);
?>