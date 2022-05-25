<?php
include 'conexion.php';

$json=array();
    if(isset($_GET["nCorreo"]) 
    && isset($_GET["nidPublicacionUsuario"])
    && isset($_GET["nReceptor"])
    && isset($_GET["nFecha"])){
        $nCorreo=$_GET['nCorreo'];
        $nidPublicacionUsuario=$_GET['nidPublicacionUsuario'];
        $nReceptor=$_GET['nReceptor'];
        $nFecha=$_GET['nFecha'];
        $consulta="CALL REG_CONVERSACION('{$nCorreo}','{$nidPublicacionUsuario}','{$nReceptor}','{$nFecha}')";
        $resultado=mysqli_query($conexion,$consulta);
        if($resultado){
            $json['Consulta']= "registrado";
        } 
        mysqli_close($conexion);
        echo json_encode($json);
    }else{
        die("Fallo la conexion");
    }
?>