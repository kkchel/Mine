package asd;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Mine extends JFrame { // ��ü
	JButton bu = null;
	boolean set;
	int a; // 0 : �⺻ 1: ��� 2: ����ǥ
	int t; // 0 �˻� x 1 �˻� o

	Mine() {
		this.set = false;
		this.a = 0;
		this.t = 0;
	}
}