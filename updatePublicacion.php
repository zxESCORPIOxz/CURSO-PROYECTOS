<?php
include 'conexion.php';

$json=array();
    try{
        if($_SERVER['REQUEST_METHOD']=='POST'){
            $nidPublicacionUsuario=$_POST['nidPublicacionUsuario'];
            $nCorreo=$_POST['nCorreo'];
            $nVariedad=$_POST['nVariedad'];
            $nCantidad=$_POST['nCantidad'];
            $nPrecio=$_POST['nPrecio'];
            $nDescripcion=$_POST['nDescripcion'];
            $nFoto=$_POST['nFoto'];
            $nfechacaducidad=$_POST['nfechacaducidad'];
    
            $nombre=rand();
            $nombre.=$nCorreo;
            $nombre.=rand();
            $PATH="micarpeta/{$nombre}.png";

            $update="CALL UPD_PUBLICACION('{$nidPublicacionUsuario}','{$nVariedad}','{$nCantidad}','{$nPrecio}','{$nDescripcion}','{$PATH}','{$nfechacaducidad}')";
            $resultado=mysqli_query($conexion,$update);
            if($resultado){
                try{
                    file_put_contents($PATH,base64_decode($nFoto));
                    echo ' realizada';
                }catch(Exception $e) {
                    echo 'Excepción capturada: ',  $e->getMessage(), "\n";
                }
            }else{
                echo 'Registro no Actualizado';
            }
        }else{
            echo 'FALLO DEL SERVIDOR';
        }
    }catch(Exception $e) {
        echo 'Excepción capturada: ',  $e->getMessage(), "\n";
    }
?>