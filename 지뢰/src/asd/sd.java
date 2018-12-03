package asd;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class sd {
	JFrame Frame = new JFrame();
	JPanel pa = new JPanel();
	JLabel[] la = new JLabel[3];
	JTextField tf[] = new JTextField[3];
	JButton bu = new JButton("확인");
	first aa = new first();

	sd() {
		for (int i = 0; i < 3; i++) {
			tf[i] = new JTextField(3);
			tf[i].setSize(30, 20);
		}
		Frame.setLayout(new GridLayout());
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame.setSize(300, 120);
		pa.setLocation(0, 0);
		pa.setSize(300, 120);
		pa.setLayout(null);

		la[0] = new JLabel("행 : ");
		la[0].setSize(30, 20);
		la[0].setLocation(40, 20);
		tf[0].setLocation(70, 20);

		la[1] = new JLabel("열 : ");
		la[1].setSize(30, 20);
		la[1].setLocation(110, 20);
		tf[1].setLocation(140, 20);

		la[2] = new JLabel("지뢰 : ");
		la[2].setSize(50, 20);
		la[2].setLocation(180, 20);
		tf[2].setLocation(220, 20);

		bu.setSize(60, 20);
		bu.setLocation(120, 50);
		bu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String row = tf[0].getText();
				if (row != null)
					aa.rows = Integer.parseInt(row);
				String col = tf[1].getText();
				if (col != null)
					aa.cols = Integer.parseInt(col);

				String mi = tf[2].getText();
				if (mi != null)
					aa.mine_s = Integer.parseInt(mi);
				System.out.println(aa.rows + "  " + aa.cols + "  " + aa.mine_s);
				aa.mine_numcheck = 3;
				Frame.setVisible(false);
				
				aa.reset();
			}
		});

		pa.add(bu);
		
		for (int i = 0; i < 3; i++) {
			pa.add(la[i]);
			pa.add(tf[i]);
		}
		Frame.add(pa);
	}

	public static void main(String[] args) {
		sd aa = new sd();

	}

}
