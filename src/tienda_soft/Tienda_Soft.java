/*
Marlon David Ortiz Ospina, Programación de Disp. Móviles 2015-2
 
Implementar el ejercicio que le fue asignado en la sustentación de java, para esto tenga en cuenta los siguientes ítems:
 Debe implementar la base de datos en un servidor online
 Almacenar el repositorio en GitHub y enviarlo al correo edwin.cubillos@udea.edu.co, el repositorio que no sea enviado a este correo no será evaluado.
 Toda la entrada de datos debe estar validada y configurada para que no se puedan entrar datos erróneos, al igual que los mensajes de advertencia, error, etc.
 La fecha y hora son el límite para el envío del correo con la url del repositorio
 */
package tienda_soft;
import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;

/**
 *
 * @author Marlangas
 */
public class Tienda_Soft {

    /**
     * @param args the command line arguments
     */
    
    // Para la conexión a la base de datos
    private static String user = "marlondavid05"; 
    private static String password = "2533553";
    private static String url = "jdbc:mysql://db4free.net/ensayo123456";
    
    private static String nombre;
    private static double cantidad, valor;
    
    private static Scanner scanf = new Scanner(System.in);
       
    public static void main(String[] args) {
         
        int accion;        
        do{
            System.out.println("Ingrese la opción que desea realizar:");
            System.out.println();
            System.out.println("1. Agregar Producto");
            System.out.println("2. Buscar Producto");
            System.out.println("3. Eliminar producto");
            System.out.println("4. Mostrar Inventario");
            System.out.println("5. Realizar Venta");
            System.out.println("6. Mostrar Ganancias totales");
            System.out.println("7. Salir");
            accion=scanf.nextInt();
            scanf.nextLine();

            switch (accion){
                    case 1:
                        Agregar();
                        break;
                    case 2:
                        Buscar();
                        break;
                    case 3:
                        Eliminar();
                        break;
                    case 4:
                        Inventario();
                        break;
                    case 5:
                        Venta();
                        break;
                    case 6:
                        Ganancias();
                        break;
                    case 7:                    
                        System.out.println("\t\t¡HASTA LA PROXIMA!");
                        break;
                    default:
                        System.out.println("OPCION NO VALIDA!!!");
                        break;    
            }
            if(accion>0 && accion<7){
                seguir();
            }
        }while(accion>0 && accion<7);       
    }
    
    public static void seguir(){
        Scanner scanf = new Scanner(System.in);          
        System.out.println();
        System.out.println();
        System.out.println("Presione ENTER para continuar");
        scanf.nextLine();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }
   
    public static void VerificarNombre(){
        Pattern p = Pattern.compile("[^A-Za-z ]");
        Matcher m = p.matcher(nombre);
        while(m.find()){
            System.out.println("ENTRADA ERRONEA, INTENTE DE NUEVO...\n");
            System.out.println("Por favor ingrese el NOMBRE del producto que desea agregar:");
            nombre=scanf.nextLine();
            m = p.matcher(nombre);
        }
    }
    
    public static double VerificarNumeros(String variable){
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(variable);
        while(m.find()){
            System.out.println("\n\nENTRADA ERRONEA!!! SE ESPERA UNA ENTRADA NUMERICA...\n");
            System.out.println("Intente Nuevamente:");
            variable=scanf.nextLine();
            m = p.matcher(variable);
        }
        return Double.parseDouble(variable); //retorna el double   
    }
    
    public static void Agregar(){
        
        try{ //Soluciona el problema de la siguiente linea cuando no logra una conexión
            System.out.println("Conectando a la base de datos...\n");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,password);
            Statement estado = con.createStatement();
            
            // Ingresar un nuevo producto
                System.out.println("Por favor ingrese el NOMBRE del producto que desea agregar:");
                nombre = scanf.nextLine();
                VerificarNombre();
                System.out.println("\nPor favor ingrese el VALOR del producto que desea agregar:");
                 valor = VerificarNumeros(scanf.nextLine());
                System.out.println("\nPor favor ingrese las CANTIDADES disponibles:");
                 cantidad = VerificarNumeros(scanf.nextLine());
            
                estado.executeUpdate("INSERT INTO `Tienda` VALUES ('"+nombre+"', '"+valor+"', '"+cantidad+"',0)");
                System.out.println("\n\t\tPRODUCTO ALMACENADO SATISFACTORIAMENTE!!!");

            }catch (SQLException ex){
                System.out.println("Error de mysql");// Error de sintaxis!!!!!!!!!!!!!!!!!!!!
            }catch(Exception e){ // no se tiene certeza cual es el error que se cometio para eso esta  +e.getMessage()
                System.out.println("Se ha encontrado un error de tipo: "+e.getMessage());
            }
    }
    
    public static void Buscar(){
        int flagbuscar=0;
        System.out.println("\nPor favor ingrese el nombre del producto que desea buscar:");
        nombre = scanf.nextLine();
        
        try{
            System.out.println("\nConectando a la base de datos...\n");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,password);
            Statement estado = con.createStatement();
            ResultSet resultado; //va contener el resultado de todas las operaciones que realice en SQL
            
            resultado = estado.executeQuery("SELECT * FROM `Tienda` WHERE `Producto` LIKE '"+nombre+"'");
            
            while(resultado.next()){// pregunta si aun hay datos en resultado (especie de lista)
                System.out.println("PRODUCTO\tPRECIO\t\tUNIDADES");
                System.out.println( resultado.getString("Producto")+"\t\t"+resultado.getString("Precio")+"\t\t"+resultado.getString("Cantidad") );
                flagbuscar=1;
            }
            
        }catch (SQLException ex){
            System.out.println("Error de mysql");// Error de sintaxis!!!!!!!!!!!!!!!!!!!!
        }catch(Exception e){ // no se tiene certeza cual es el error que se cometio para eso esta  +e.getMessage()
            System.out.println("Se ha encontrado un error de tipo: "+e.getMessage());
        }
        if(flagbuscar==0){
            System.out.println("\n\t\tEL PRODUCTO QUE BUSCA NO SE ENCUENTRA EN LA TIENDA!!!");
        }
    }
    
    public static void Eliminar(){
        int flageliminar=0;
        
        System.out.println("\nPor favor ingrese el nombre del producto que desea Eliminar:");
        nombre=scanf.nextLine();
        
        try{
            System.out.println("\nConectando a la base de datos...\n");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,password);
            Statement estado = con.createStatement();
            
            flageliminar=estado.executeUpdate("DELETE FROM `Tienda` WHERE `Producto` LIKE '"+nombre+"'");
            if(flageliminar!=0){
                System.out.println("\n\t\tPRODUCTO ELIMINADO SATISFACTORIAMENTE!!!");
            } 
        }catch (SQLException ex){
            System.out.println("Error de mysql");// Error de sintaxis!!!!!!!!!!!!!!!!!!!!
        }catch(Exception e){ // no se tiene certeza cual es el error que se cometio para eso esta  +e.getMessage()
            System.out.println("Se ha encontrado un error de tipo: "+e.getMessage());
        }
        if(flageliminar==0){
            System.out.println("\n\t\tEL PRODUCTO QUE BUSCA NO SE ENCUENTRA EN LA TIENDA!!!");
        }   
    }
    
    public static void Inventario(){
        int flaginventario=0;
        try{
            System.out.println("\nConectando a la base de datos...\n");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,password);
            Statement estado = con.createStatement();
            ResultSet resultado; //va contener el resultado de todas las operaciones que realice en SQL
            
            resultado = estado.executeQuery("SELECT * FROM `Tienda` "); //Carga todos los productos
            System.out.println("PRODUCTO\tPRECIO\t\tUNIDADES");
            while(resultado.next()){// pregunta si aun hay datos en resultado (especie de lista)
                System.out.println( resultado.getString("Producto")+"\t\t"+resultado.getString("Precio")+"\t\t"+resultado.getString("Cantidad") );
                flaginventario=1;
            }           
        }catch (SQLException ex){
            System.out.println("Error de mysql");// Error de sintaxis!!!!!!!!!!!!!!!!!!!!
        }catch(Exception e){ // no se tiene certeza cual es el error que se cometio para eso esta  +e.getMessage()
            System.out.println("Se ha encontrado un error de tipo: "+e.getMessage());
        }
        if(flaginventario==0){
            System.out.println("\n\n\t\tAUN NO SE HAN INGRESADO PRODUCTOS A LA TIENDA!!!");
        }
    }
    
    public static void Venta(){
        int flagventas=0;
        String ventas="0",cantidad="0";
        System.out.println("\nPor favor ingrese el nombre del producto que desea vender:");
        nombre=scanf.nextLine();
    
        try{
            System.out.println("\nConectando a la base de datos...\n");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,password);
            Statement estado = con.createStatement();
            ResultSet resultado; //va contener el resultado de todas las operaciones que realice en SQL
            
            resultado = estado.executeQuery("SELECT * FROM `Tienda` WHERE `Producto` LIKE '"+nombre+"'");
                
           while(resultado.next()){// pregunta si aun hay datos en resultado (especie de lista)
                ventas=resultado.getString("Ventas"); 
                cantidad=resultado.getString("Cantidad");
                flagventas=1;
            }
            estado.executeUpdate("UPDATE `ensayo123456`.`Tienda` SET `Ventas` = '"+( Double.parseDouble(ventas)+1 )+"' WHERE `Tienda`.`Producto` LIKE '"+nombre+"'");
           
            if((Double.parseDouble(cantidad)-1) >= 0){
                estado.executeUpdate("UPDATE `ensayo123456`.`Tienda` SET `Cantidad` = '"+( Double.parseDouble(cantidad)-1 )+"' WHERE `Tienda`.`Producto` LIKE '"+nombre+"'");
            }else{
                flagventas=0;
                System.out.println("\n\t\tSE AGOTARON EXISTENCIAS!!!");
            }            
        }catch (SQLException ex){
            System.out.println("Error de mysql");// Error de sintaxis!!!!!!!!!!!!!!!!!!!!
        }catch(Exception e){ // no se tiene certeza cual es el error que se cometio para eso esta  +e.getMessage()
            System.out.println("Se ha encontrado un error de tipo: "+e.getMessage());
        }
        if(flagventas==0){
            System.out.println("\n\t\tEL PRODUCTO QUE BUSCA NO SE ENCUENTRA EN LA TIENDA!!!");
        }else{
            System.out.println("\n\n\t\tVENTA EXITOSA!!!");
        }
    }
    
    public static void Ganancias(){
        double ganancias=0;
        int flaganancias=0;
         try{
            System.out.println("\nConectando a la base de datos...\n");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,password);
            Statement estado = con.createStatement();
            ResultSet resultado; //va contener el resultado de todas las operaciones que realice en SQL
            
            
            
            resultado = estado.executeQuery("SELECT * FROM `Tienda` "); //Carga todos los productos
            System.out.println("PRODUCTO\tPRECIO\t\tUNIDADES DISP.\t\tUNDS VENDIDAS\t\tTOTAL VENTA");
            while(resultado.next()){// pregunta si aun hay datos en resultado (especie de lista)
                if(Double.parseDouble(resultado.getString("Ventas"))!= 0){
                    System.out.println( resultado.getString("Producto")+"\t\t"+resultado.getString("Precio")+"\t\t"
                    +resultado.getString("Cantidad")+"\t\t\t"+resultado.getString("Ventas")+"\t\t\t$"
                    +(Double.parseDouble(resultado.getString("Precio"))*Double.parseDouble(resultado.getString("Ventas")) ) );            
                    ganancias=Double.parseDouble(resultado.getString("Precio"))*Double.parseDouble(resultado.getString("Ventas"))+ganancias;        
                    flaganancias=1;
                }
            }
            System.out.println("\n\nLas Ganancias Totales son: $"+ganancias);
        }catch (SQLException ex){
            System.out.println("Error de mysql");// Error de sintaxis!!!!!!!!!!!!!!!!!!!!
        }catch(Exception e){ // no se tiene certeza cual es el error que se cometio para eso esta  +e.getMessage()
            System.out.println("Se ha encontrado un error de tipo: "+e.getMessage());
        } 
        if(flaganancias == 0){
            System.out.println("\n\n\t\tNO SE HA REALIZADO NINGUNA VENTA!!!");
        }
    }  
}