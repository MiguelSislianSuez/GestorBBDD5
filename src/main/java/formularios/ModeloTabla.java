package formularios;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;



public class ModeloTabla extends DefaultTableModel {//mostrará las tablas
	
	
	
	
	public ModeloTabla(ResultSet rs) {
		
		try {
			
		ResultSetMetaData rsmd = rs.getMetaData();
		
		int cols = rsmd.getColumnCount();//con este meteodo sabemos el numero de columnas que encesitamos. 
		
		//Hacemos un bucle recorriendo las col de la bbdd
		for (int i = 1; i <= cols; i++) {
			addColumn(rsmd.getColumnName(i));
		}
		while(rs.next()) {
			//irá creando una fila en esta tabla con los datos
			Object[] row = new Object[cols];//array para con elnumero de columnas que esta en cols
			
			for(int i = 1; i <= cols; i++) {
				
				//Metemos cada eemento en una columna de esta fila
				row [i - 1] = rs.getObject(i);//el primer elemento que guarde será en i - 1, osea 0. Sino dará indexautobound exception porque terminaria antes que el array 
				
			}
			
			addRow(row);
			UIManager.put("Table.gridColor", new ColorUIResource(Color.gray));
		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public boolean isCellEditable(int row, int column) {//evitamos que la tabla sea editable
		return false;
	}
	
}
