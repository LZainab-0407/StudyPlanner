package views;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SearchView extends JPanel{
	
	public SearchView(JPanel mainContent, JFrame parent) {
		
		this.setLayout(new BorderLayout(10, 10));
		
		SearchPanel searchPanel = new SearchPanel(mainContent, parent);
		this.add(searchPanel, BorderLayout.NORTH);
	}
}
