<?php
include 'conexion.php';

$json=array();
    if(isset($_GET["nidConversacion"])
    && isset($_GET["nidUsuario"]) ){
        $nidConversacion=$_GET['nidConversacion'];
        $nidUsuario=$_GET['nidUsuario'];
        $consulta="CALL CON_UPD_MENSAJE('{$nidConversacion}','{$nidUsuario}')";
        $resultado=mysqli_query($conexion,$consulta);
        while($fila1 = mysqli_fetch_array($resultado)){
            $json['Consulta'][]= $fila1;
        }
        mysqli_close($conexion);
        echo json_encode($json);
    }else{
        die("Fallo la conexion");
    }
?>