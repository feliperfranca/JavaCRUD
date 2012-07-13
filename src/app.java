import javax.swing.JFrame;

import com.crud.gui.MainWindow;


public class app {

	public static void main(String[] args) {
		MainWindow mainWindow = new MainWindow();
		mainWindow.setVisible(true);
		mainWindow.setResizable(false);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
