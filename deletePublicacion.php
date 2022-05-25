<?php
include 'conexion.php';

$json=array();
    if($_SERVER['REQUEST_METHOD']=='POST'){
        $nidPublicacionUsuario=$_POST['nidPublicacionUsuario'];
        $delete="CALL DEL_PUBLICACION('{$nidPublicacionUsuario}')";
        $resultado_delete=mysqli_query($conexion,$delete);
        if($resultado_delete){
            echo 'Eliminado correctamente';
        }
    }
?>