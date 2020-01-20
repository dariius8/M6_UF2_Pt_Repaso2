package metodos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Metodos {

	static final String url = "jdbc:mysql://localhost:3306/empleados_departamentos";

	public static void menu() {
		Scanner lector = new Scanner(System.in);
		int i = 0;
		while (i != 7) {
			System.out.println("\nMENU");
			System.out.println("   ---EMPLEADOS---");
			System.out.println("   1. Insertar empleado");
			System.out.println("   2. Actualizar empleado");
			System.out.println("   3. Borrar empleado");
			System.out.println("\n   ---DEPARTAMENTOS---");
			System.out.println("   4. Insertar departamento");
			System.out.println("   5. Actualizar departamento");
			System.out.println("   6. Borrar departamento");
			System.out.println("\n   7. Salir");
			System.out.print("Elige una opcion: ");
			i = lector.nextInt();
			if (i > 0 && i < 8) {
				switch (i) {
				case 1:
					insertarEmpleado();
					break;
				case 2:
					actualizarEmpleado();
					break;
				case 3:
					borrarEmpleado();
					break;
				case 4:
					insertarDepartamento();
					break;
				case 5:
					actualizarDepartamento();
					break;
				case 6:
					borrarDepartamento();
					break;
				default:
					System.out.println("\nAdios!");
					break;
				}
			} else
				System.out.println("\nError! Valor incorrecto.");
		}
	}

	public static void insertarDepartamento() {
		Scanner lectorInt = new Scanner(System.in);
		Scanner lectorString = new Scanner(System.in);
		try {
			// conexion
			Connection conn = DriverManager.getConnection(url, "root", "");
			// insert departamento
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO departamentos VALUES(?, ?, ?)");
			// pedimos los datos por consola
			System.out.println("\nInserta el numero de departamento:");
			int dept_no = lectorInt.nextInt();
			pstmt.setInt(1, dept_no);
			System.out.println("Inserta el nombre:");
			String dnombre = lectorString.nextLine();
			pstmt.setString(2, dnombre);
			System.out.println("Inserta la localizacion:");
			String loc = lectorString.nextLine();
			pstmt.setString(3, loc);
			pstmt.executeUpdate();
			System.out.println("\nDepartamento '" + dnombre + "' insertado correctamente.");
			// select tabla departamentos para ver los cambios
			pstmt = conn.prepareStatement("SELECT * FROM departamentos");
			ResultSet rs = pstmt.executeQuery();
			System.out.println("\n---Tabla departamentos---");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));
			}
			conn.close();
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insertarEmpleado() {
		Scanner lectorInt = new Scanner(System.in);
		Scanner lectorString = new Scanner(System.in);
		Scanner lectorFloat = new Scanner(System.in);
		try {
			// conexion
			Connection conn = DriverManager.getConnection(url, "root", "");
			// insert empleado pasandole datos
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO empleados VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
			// pedimos los datos por consola
			System.out.println("\nInserta el numero de empleado:");
			int emp_no = lectorInt.nextInt();
			pstmt.setInt(1, emp_no);
			System.out.println("Inserta el apellido:");
			String apellido = lectorString.nextLine();
			pstmt.setString(2, apellido);
			System.out.println("Inserta el oficio:");
			String oficio = lectorString.nextLine();
			pstmt.setString(3, oficio);
			System.out.println("Inserta el codigo de director:");
			int dir = lectorInt.nextInt();
			pstmt.setInt(4, dir);
			// fecha string -> date
			System.out.println("Inserta la fecha de alta (yyyy-MM-dd):");
			String fecha = lectorString.nextLine();
			Date parse_fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
			java.sql.Date fecha_alta = new java.sql.Date(parse_fecha.getTime());
			pstmt.setDate(5, fecha_alta);
			System.out.println("Inserta el salario:");
			float salario = lectorFloat.nextFloat();
			pstmt.setDouble(6, salario);
			System.out.println("Inserta la comision:");
			float comision = lectorFloat.nextFloat();
			pstmt.setDouble(7, comision);
			System.out.println("Inserta el numero de departamento:");
			int dept_no = lectorInt.nextInt();
			pstmt.setInt(8, dept_no);
			// select count departamento (pasandolo por consola)
			PreparedStatement pstmt2 = conn.prepareStatement("SELECT COUNT(*) FROM departamentos WHERE dept_no = ?");
			pstmt2.setInt(1, dept_no);
			ResultSet rs = pstmt2.executeQuery();
			// resultset tabla departamentos
			if (rs.next()) {
				// si existe el departamento
				if (rs.getInt(1) == 1) {
					// insertamos el empleado
					pstmt.executeUpdate();
					System.out.println("\nEmpleado '" + apellido + "' insertado correctamente.");
					// select tabla empleados para ver los cambios
					pstmt = conn.prepareStatement("SELECT * FROM empleados");
					ResultSet rs2 = pstmt.executeQuery();
					System.out.println("\n---Tabla empleados---");
					while (rs2.next()) {
						System.out.println(rs2.getInt(1) + "\t" + rs2.getString(2) + "\t" + rs2.getString(3) + "\t"
								+ rs2.getInt(4) + "\t" + rs2.getDate(5) + "\t" + rs2.getDouble(6) + "\t"
								+ rs2.getDouble(7) + "\t" + rs2.getInt(8));
					}
					rs2.close();
				} else
					System.out.println("\nNo existe ningun departamento con ese numero.");
			}
			conn.close();
			pstmt.close();
			pstmt2.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println(e + "\nError! El formato de la fecha es incorrecto.");
		}
	}

	public static void actualizarDepartamento() {
		Scanner lectorString = new Scanner(System.in);
		Scanner lectorInt = new Scanner(System.in);
		System.out.println("\nInserta el nombre de departamento:");
		String dnombre = lectorString.nextLine();
		System.out.println("Inserta el nuevo numero de departamento:");
		int dept_no = lectorInt.nextInt();
		try {
			// conexion
			Connection conn = DriverManager.getConnection(url, "root", "");
			// select empleados de ese departamento pasandole el nombre
			PreparedStatement pstmt = conn.prepareStatement("SELECT dept_no FROM empleados WHERE dept_no = "
					+ "(SELECT dept_no FROM departamentos WHERE dnombre = ?)");
			pstmt.setString(1, dnombre);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int dept_no_viejo = rs.getInt(1);
				// update del dept_no cambiamos el antiguo por el que le pasamos
				pstmt = conn.prepareStatement("UPDATE empleados SET dept_no = ? WHERE dept_no = ?");
				pstmt.setInt(1, dept_no);
				pstmt.setInt(2, dept_no_viejo);
				pstmt.executeUpdate();
				System.out.println("\nEmpleados con el mismo numero de departamento actualizados.");
			}
			// update departamentos pasandole el nombre de departamento
			pstmt = conn.prepareStatement("UPDATE departamentos SET dept_no = ? WHERE dnombre = ?");
			pstmt.setInt(1, dept_no);
			pstmt.setString(2, dnombre);
			pstmt.executeUpdate();
			System.out.println("\nDepartamento '" + dnombre + "' actualizado con el numero " + dept_no + ".");
			conn.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void actualizarEmpleado() {
		Scanner lectorInt = new Scanner(System.in);
		Scanner lectorFloat = new Scanner(System.in);
		System.out.println("\nInserta el numero de empleado:");
		int emp_no = lectorInt.nextInt();
		System.out.println("Inserta el nuevo salario:");
		float salario = lectorFloat.nextFloat();
		try {
			// conexion
			Connection conn = DriverManager.getConnection(url, "root", "");
			// update del salario pasandole el numero de empleado
			PreparedStatement pstmt = conn.prepareStatement("UPDATE empleados SET salario = ? WHERE emp_no = ?");
			pstmt.setFloat(1, salario);
			pstmt.setInt(2, emp_no);
			pstmt.executeUpdate();
			System.out.println("\nSalario actualizado " + salario + " del empleado " + emp_no + ".");
			conn.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void borrarDepartamento() {
		Scanner lectorInt = new Scanner(System.in);
		System.out.println("\nInserta el numero de departamento:");
		int dept_no = lectorInt.nextInt();
		try {
			// conexion
			Connection conn = DriverManager.getConnection(url, "root", "");
			// select count departamento (pasandolo por consola)
			PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM departamentos WHERE dept_no = ?");
			pstmt.setInt(1, dept_no);
			ResultSet rs = pstmt.executeQuery();
			// select count empleados de ese departamento
			PreparedStatement pstmt2 = conn.prepareStatement("SELECT COUNT(*) FROM empleados WHERE dept_no = ?");
			pstmt2.setInt(1, dept_no);
			ResultSet rs2 = pstmt2.executeQuery();
			// resultset tabla departamentos
			if (rs.next()) {
				// si existe el departamento (vacio o con empleados)
				if (rs.getInt(1) == 1) {
					// resultset tabla empleados
					if (rs2.next()) {
						// si el departamento esta sin empleados
						if (rs2.getInt(1) == 0) {
							// lo borramos pasandole el numero de departamento
							pstmt2 = conn.prepareStatement("DELETE FROM departamentos WHERE dept_no = ?");
							pstmt2.setInt(1, dept_no);
							pstmt2.executeUpdate();
							System.out.println("\nDepartamento borrado correctamente.");
						} else
							System.out.println("\nNo se puede borrar el departamento porque tiene empleados.");
					}
				} else
					System.out.println("\nNo existe ningun departamento con ese numero.");
			}
			conn.close();
			pstmt.close();
			rs.close();
			pstmt2.close();
			rs2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void borrarEmpleado() {
		Scanner lectorInt = new Scanner(System.in);
		System.out.println("\nInserta el numero de empleado:");
		int emp_no = lectorInt.nextInt();
		try {
			// conexion
			Connection conn = DriverManager.getConnection(url, "root", "");
			// select pasandole el numero de empleado
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM empleados WHERE emp_no = ?");
			pstmt.setInt(1, emp_no);
			ResultSet rs = pstmt.executeQuery();
			// si no hay datos
			if (rs.next() == false)
				System.out.println("\nNo existe ningun empleado con ese numero.");
			else {
				// delete pasandole el numero de empleado
				pstmt = conn.prepareStatement("DELETE FROM empleados WHERE emp_no = ?");
				pstmt.setInt(1, emp_no);
				pstmt.executeUpdate();
				System.out.println("\nEmpleado borrado correctamente.");
			}
			conn.close();
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
