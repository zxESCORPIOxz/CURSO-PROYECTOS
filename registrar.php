<?php
include 'conexion.php';
//http://localhost/api/registrar.php?nCorreo=71806587@continental.edu.pe&nPass=12345678&nDni=71806587&nApellido=Duran&nNombre=Kevin&nDirecion=Calle sin nombre&nLatitud=-22&nLongitud=-76&nTelefono=998973240
$json=array();

    if(isset($_GET["nCorreo"]) 
    && isset($_GET["nDni"]) 
    && isset($_GET["nApellido"]) 
    && isset($_GET["nNombre"]) 
    && isset($_GET["nDirecion"]) 
    && isset($_GET["nLatitud"]) 
    && isset($_GET["nLongitud"]) 
    && isset($_GET["nTelefono"])){
        $nCorreo=$_GET['nCorreo'];
        $nDni=$_GET['nDni'];
        $nApellido=$_GET['nApellido'];
        $nNombre=$_GET['nNombre'];
        $nDirecion=$_GET['nDirecion'];
        $nLatitud=$_GET['nLatitud'];
        $nLongitud=$_GET['nLongitud'];
        $nTelefono=$_GET['nTelefono'];

        $insert="CALL REG_USUARIO('{$nCorreo}','{$nDni}','{$nApellido}','{$nNombre}','{$nDirecion}','{$nLatitud}','{$nLongitud}','{$nTelefono}')";
        $resultado_insert=mysqli_query($conexion,$insert);

        if($resultado_insert){
            $json['usuario']="Registrado";
            mysqli_close($conexion);
            echo json_encode($json);
        }
        else{
            $json['usuario']="No Registrado";
            mysqli_close($conexion);
            echo json_encode($json);
        }
    }
    else{
        $json['usuario']="Fallo de conexion";
        mysqli_close($conexion);
        echo json_encode($json);
    }


?>