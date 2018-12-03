package asd;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Mine extends JFrame { // 객체
	JButton bu = null;
	boolean set;
	int a; // 0 : 기본 1: 깃발 2: 물음표
	int t; // 0 검색 x 1 검색 o

	Mine() {
		this.set = false;
		this.a = 0;
		this.t = 0;
	}
}