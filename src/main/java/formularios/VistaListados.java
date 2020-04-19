package formularios;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class VistaListados extends JDialog {
	
	private ResultSet rs;//llamamos al result



	public VistaListados(ResultSet rs) {
		super();
		this.rs = rs;
		this.setSize( 1000 , 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		setVisible(true);
		
		getlistado();
		
		try {
			this.setTitle(rs.getMetaData().getTableName(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	private void getlistado() {

		ModeloTabla mt = new ModeloTabla(rs);
		JTable tabla = new JTable(mt);
		tabla.setFillsViewportHeight(true);
		tabla.setAutoCreateRowSorter(true);
		
		JScrollPane sp = new JScrollPane(tabla);
		add(sp, BorderLayout.CENTER);
		
	}

	
	
	

}
