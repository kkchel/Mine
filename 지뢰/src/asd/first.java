package asd;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;

// 화면,메인
public class first extends JFrame {
	Dimension dimen, dimen1; // 화면크기
	int xpos, ypos; // 좌표
	// 지뢰이미지
	ImageIcon mine_c = new ImageIcon("img/close.gif");
	ImageIcon mine_p = new ImageIcon("img/pressed.gif");
	ImageIcon mine_f = new ImageIcon("img/flag.gif");
	ImageIcon mine_w = new ImageIcon("img/wildcard.gif");
	ImageIcon mine_m1 = new ImageIcon("img/mine1.gif");
	ImageIcon mine_m2 = new ImageIcon("img/mine2.gif");
	ImageIcon mine_m3 = new ImageIcon("img/mine3.gif");
	ImageIcon smile = new ImageIcon("img/smile.png");
	ImageIcon num_img[] = new ImageIcon[7];
	ImageIcon num_img1[] = new ImageIcon[10];
	JLabel count_mine[] = new JLabel[3];
	JLabel count_time[] = new JLabel[3];
	sd aaa = null;
	int mine_s = 0;

	JPanel panel1, panel2; // panel1 = 상단부 panel2 = 하단부
	JButton bu = new JButton(smile);
	JFrame frame; // 본프레임
	int easy = 9, normal = 15, hard = 20;
	int mine_num = easy; // 지뢰갯수
	int mine_numcheck = 0; // 0 이지 1 노말 2 하드
	int rows = easy, cols = easy; // 행렬
	Mine mine[][]; // 객체 선언

	boolean asdd = false; // 창크기 조절용
	boolean game_start = false; // 게임 시작 유무
	int time_check = 0;
	time_set ggg = new time_set();

	first() {
		ggg.setDaemon(false);
		ggg.start();

		for (int i = 1; i < 8; i++)
			num_img[i - 1] = new ImageIcon("img/" + i + "s.gif");
		for (int i = 0; i < 10; i++)
			num_img1[i] = new ImageIcon("img/" + i + "n.gif");

		// GUI
		frame = new JFrame("지뢰찾기");
		createMenu();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLayout(null);
		frame.setIconImage(new ImageIcon("img/icon.gif").getImage());
		panel1 = new JPanel(); // 페널1 - 상단부
		panel2 = new JPanel(); // 페널2 - 하단부

		num_position();
		panel1.setLayout(null);
		panel1.setLocation(0, 0);
		panel1.setSize(rows * mine_c.getIconWidth() + 15, 38);

		frame.add(panel1, BorderLayout.NORTH);
		bu.addActionListener(new ActionListener() { // 새게임 이벤트
			public void actionPerformed(ActionEvent e) {
				JButton bu = (JButton) e.getSource();
				if (bu.isEnabled() == true) {
					reset();
				}
			}
		});

		mine = new Mine[rows][cols]; // 객체 초기화

		panel2.setLayout(null);
		panel2.setSize(rows * mine_c.getIconWidth() + 20, 20 + mine_c.getIconHeight() * cols);
		panel2.setLocation(0, 38);
		// 버튼 설정
		for (int x = 0; x < rows; x++)
			for (int y = 0; y < cols; y++) {
				mine[x][y] = new Mine();
				mine[x][y].bu = new JButton(mine_c);
				mine[x][y].bu.setPressedIcon(mine_p);
				mine[x][y].bu.setSize(mine_c.getIconWidth(), mine_c.getIconHeight());
				mine[x][y].bu.setBounds(5 + x * mine_c.getIconWidth(), 5 + y * mine_c.getIconHeight(),
						mine_c.getIconWidth(), mine_c.getIconHeight());
				mine[x][y].bu.setRolloverEnabled(false);
				mine[x][y].bu.addActionListener(new MineActionListener());
				mine[x][y].bu.setDisabledIcon(null);
				panel2.add(mine[x][y].bu);
				mine[x][y].bu.addMouseListener(new masdf());
			}
		mine_positioning(mine_num, mine); // 지뢰 깔기
		minenum_position();
		frame.add(panel2);
		frame.setSize(rows * mine_c.getIconWidth() + 17, 102 + mine_c.getIconHeight() * cols);
		setlocation();
		frame.setLocation(xpos, ypos);

	}

	void reset() { // 지뢰 초기화
		game_start = false;
		panel2.removeAll();
		mine = new Mine[rows][cols];
		for (int x = 0; x < mine.length; x++)
			for (int y = 0; y < mine[x].length; y++) {
				mine[x][y] = new Mine();
				mine[x][y].bu = new JButton(mine_c);
				mine[x][y].bu.setPressedIcon(mine_p);
				mine[x][y].bu.setSize(mine_c.getIconWidth(), mine_c.getIconHeight());
				mine[x][y].bu.setBounds(5 + x * mine_c.getIconWidth(), 5 + y * mine_c.getIconHeight(),
						mine_c.getIconWidth(), mine_c.getIconHeight());
				mine[x][y].bu.setRolloverEnabled(false);
				mine[x][y].bu.addActionListener(new MineActionListener());
				mine[x][y].bu.setDisabledIcon(null);
				panel2.add(mine[x][y].bu);
				mine[x][y].bu.addMouseListener(new masdf());
			}
		for (int i = 0; i < 3; i++) {
			panel1.remove(count_mine[i]);
			panel1.remove(count_time[i]);
		}
System.out.println(mine_numcheck);
		switch (mine_numcheck) {
		case 0:
			mine_num = easy;
			break;
		case 1:
			mine_num = normal;
			break;
		case 2:
			mine_num = hard;
			break;
		case 3:
			mine_num = mine_s;
		}
		mine_positioning(mine_num, mine);
		num_position();
		panel1.setSize(rows * mine_c.getIconWidth() + 15, 38);
		panel2.setSize(rows * mine_c.getIconWidth() + 20, 20 + mine_c.getIconHeight() * cols);
		if (asdd) {
			frame.setSize(rows * mine_c.getIconWidth() + 17, 102 + mine_c.getIconHeight() * cols);
			asdd = false;
		} else {
			frame.setSize(rows * mine_c.getIconWidth() + 18, 103 + mine_c.getIconHeight() * cols);
			asdd = true;
		}
		System.out.println(mine_num);
	}

	class MineActionListener implements ActionListener { // 지뢰엑션
		public void actionPerformed(ActionEvent e) {
			start();
			int x, y;
			JButton b = (JButton) e.getSource();
			x = (b.getX() - 5) / 17;
			y = (b.getY() - 5) / 18;
			if (mine[x][y].bu.isEnabled() == true) {
				b.setEnabled(false);
				b.setIcon(mine_c);
				if (mine[x][y].set == true) {
					for (int i = 0; i < mine.length; i++)
						for (int j = 0; j < mine[x].length; j++)
							if (mine[i][j].set == true)
								mine[i][j].bu.setIcon(mine_m1);
					b.setEnabled(true);
					b.setIcon(mine_m2);
					gameover();
				}

				else
					search(x, y, 0);

			}
			clear();
		}
	}

	class masdf implements MouseListener { // 마우스 이벤트
		boolean left = false, right = false;
		int x1, y1;
		int i, j;

		public void mouseClicked(MouseEvent e) {
			setlocation();
			int x = (e.getXOnScreen() - panel2.getLocationOnScreen().x - 5) / 17;
			int y = (e.getYOnScreen() - panel2.getLocationOnScreen().y - 5) / 18;
			if (e.getButton() == 3)
				switch (mine[x][y].a) {
				case 0:
					mine[x][y].bu.setEnabled(false);
					mine[x][y].bu.setDisabledIcon(mine_f);
					mine[x][y].a++;
					mine[x][y].t = 1;
					mine_num--;
					minenum_position();
					break;
				case 1:
					mine[x][y].bu.setDisabledIcon(mine_w);
					mine[x][y].a++;
					mine_num++;
					minenum_position();
					break;
				case 2:
					mine[x][y].bu.setEnabled(true);
					mine[x][y].bu.setDisabledIcon(null);
					mine[x][y].a = 0;
					mine[x][y].t = 0;
					break;
				}

		}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			int x = (e.getXOnScreen() - panel2.getLocationOnScreen().x - 5) / 17;
			int y = (e.getYOnScreen() - panel2.getLocationOnScreen().y - 5) / 18;
			x1 = x;
			y1 = y;
			if (e.getButton() == 1)
				left = true;
			if (e.getButton() == 3)
				right = true;
			if (left && right) {
				around(mine_p, x, y);
			}
		}
		public void mouseReleased(MouseEvent e) {
			int x = (e.getXOnScreen() - panel2.getLocationOnScreen().x - 5) / 17;
			int y = (e.getYOnScreen() - panel2.getLocationOnScreen().y - 5) / 18;
			if (e.getButton() == 1) {
				left = false;
				right = false;
			}
			if (e.getButton() == 3) {
				right = false;
				if (left && !right) {
					i = around(mine_c, x1, y1); // 주변 지뢰갯수
					if (x1 == x && y1 == y) { // getplag는 깃발이 아닌데 주변에 지뢰인곳이 있는가
												// 0 있다 나머지 없다
						j = asdf(x, y); // 주변 깃발
						if (i == 0)
							if (getplag(x, y) == 0)
								search_around(x, y);
					}
				}
				left = false;
			}
		}
	}

	Random random = new Random();

	void gameover() { // 게임 종료
		int result = JOptionPane.showConfirmDialog(null, "지뢰터짐", "실패", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.CLOSED_OPTION)
			;
		else if (result == JOptionPane.YES_OPTION)
			reset();
		else
			System.exit(1);

	}

	void mine_positioning(int num, Mine[][] mine) { // 지뢰 깔기

		int x, y;
		for (x = 0; x < mine.length; x++)
			for (y = 0; y < mine[x].length; y++)
				mine[x][y].set = false;

		int i = 0;
		while (i < num) {
			x = random.nextInt(mine.length);
			y = random.nextInt(mine[x].length);
			if (!mine[x][y].set) {
				mine[x][y].set = true;
			} else
				i--;
			i++;
		}

	}

	int around(ImageIcon img, int x, int y) { // 양클릭시 주변 이미지바꿈
		int i = 0;
		if (x != 0) {
			if (mine[x - 1][y].t == 0) {
				mine[x - 1][y].bu.setIcon(img);
				if (mine[x - 1][y].set == true)
					++i;
			}
			if (y != 0 && mine[x - 1][y - 1].t == 0) {
				mine[x - 1][y - 1].bu.setIcon(img);
				if (mine[x - 1][y - 1].set == true)
					++i;
			}
			if (y != mine[x].length - 1 && mine[x - 1][y + 1].t == 0) {
				mine[x - 1][y + 1].bu.setIcon(img);
				if (mine[x - 1][y + 1].set == true)
					++i;
			}
		}
		if (x != mine.length - 1) {
			if (mine[x + 1][y].t == 0) {
				mine[x + 1][y].bu.setIcon(img);
				if (mine[x + 1][y].set == true)
					++i;
			}
			if (y != 0 && mine[x + 1][y - 1].t == 0) {
				mine[x + 1][y - 1].bu.setIcon(img);
				if (mine[x + 1][y - 1].set == true)
					++i;
			}
			if (y != mine[x].length - 1 && mine[x + 1][y + 1].t == 0) {
				mine[x + 1][y + 1].bu.setIcon(img);
				if (mine[x + 1][y + 1].set == true)
					++i;
			}
		}
		if (y != 0 && mine[x][y - 1].t == 0) {
			mine[x][y - 1].bu.setIcon(img);
			if (mine[x][y - 1].set == true)
				++i;
		}
		if (y != mine[x].length - 1 && mine[x][y + 1].t == 0) {
			mine[x][y + 1].bu.setIcon(img);
			if (mine[x][y + 1].set == true)
				++i;
		}
		return i;
	}

	int getplag(int x, int y) { // 주변 깃발갯수
		int p = 0;
		if (x != 0) {
			if (!(mine[x - 1][y].a == 1) && mine[x - 1][y].set)
				p++;
			if (y != 0)
				if (!(mine[x - 1][y - 1].a == 1) && mine[x - 1][y - 1].set)
					p++;
			if (y != mine[x].length - 1)
				if (!(mine[x - 1][y + 1].a == 1) && mine[x - 1][y + 1].set)
					p++;
		}
		if (x != mine.length - 1) {
			if (!(mine[x + 1][y].a == 1) && mine[x + 1][y].set)
				p++;
			if (y != 0)
				if (!(mine[x + 1][y - 1].a == 1) && mine[x + 1][y - 1].set)
					p++;
			if (y != mine[x].length - 1)
				if (!(mine[x + 1][y + 1].a == 1) && mine[x + 1][y + 1].set)
					p++;
		}
		if (y != 0)
			if (!(mine[x][y - 1].a == 1) && mine[x][y - 1].set)
				p++;
		if (y != mine[x].length - 1)
			if (!(mine[x][y + 1].a == 1) && mine[x][y + 1].set)
				p++;

		return p;
	}

	int asdf(int x, int y) {
		int p = 0;
		if (x != 0) {
			if (mine[x - 1][y].a == 1)
				p++;
			if (y != 0)
				if (mine[x - 1][y - 1].a == 1)
					p++;
			if (y != mine[x].length - 1)
				if (mine[x - 1][y + 1].a == 1)
					p++;
		}
		if (x != mine.length - 1) {
			if (mine[x + 1][y].a == 1)
				p++;
			if (y != 0)
				if (mine[x + 1][y - 1].a == 1)
					p++;
			if (y != mine[x].length - 1)
				if (mine[x + 1][y + 1].a == 1)
					p++;
		}
		if (y != 0)
			if (mine[x][y - 1].a == 1)
				p++;
		if (y != mine[x].length - 1)
			if (mine[x][y + 1].a == 1)
				p++;

		return p;
	}

	void num_position() { // 번호판 셋팅
		for (int i = 0; i < 3; i++) {
			count_time[i] = new JLabel(num_img1[0]);
			count_time[i].setLocation(5 + i * 18, 10);
			count_time[i].setSize(18, 20);
			panel1.add(count_time[i]);
		}
		bu.setSize(25, 25);
		bu.setLocation((rows * mine_c.getIconWidth() + 15) / 2 - 12, 10);
		panel1.add(bu);

		for (int i = 0; i < 3; i++) {
			count_mine[i] = new JLabel();
			count_mine[i].setLocation(rows * mine_c.getIconWidth() - 10 - i * 18, 10);
			count_mine[i].setSize(18, 20);
			panel1.add(count_mine[i]);
		}
		minenum_position();

	}

	void minenum_position() {/////////////////////////////////////////////////////////
		String s = mine_num + "";
		if (s.length() == 0)
			for (int i = 0; i < 3; i++)
				count_mine[i].setIcon(num_img1[0]);
		else if (s.length() == 1) {
			for (int i = 1; i < 3; i++)
				count_mine[i].setIcon(num_img1[0]);
			count_mine[0].setIcon(num_img1[mine_num]);
		} else if (s.length() == 2) {
			count_mine[0].setIcon(num_img1[mine_num % 10]);
			count_mine[1].setIcon(num_img1[mine_num / 10]);
			count_mine[2].setIcon(num_img1[0]);
		} else {
			count_mine[0].setIcon(num_img1[mine_num % 10]);
			count_mine[1].setIcon(num_img1[mine_num / 10]);
			count_mine[2].setIcon(num_img1[mine_num / 100]);
		}
	}

	void timenum_position() {/////////////////////////////////////////////////////////
		String s = time_check + "";
		if (s.length() == 0)
			for (int i = 0; i < 3; i++)
				count_time[i].setIcon(num_img1[0]);
		else if (s.length() == 1) {
			for (int i = 0; i < 2; i++)
				count_time[i].setIcon(num_img1[0]);
			count_time[2].setIcon(num_img1[time_check]);
		} else if (s.length() == 2) {
			count_time[0].setIcon(num_img1[0]);
			count_time[1].setIcon(num_img1[time_check / 10]);
			count_time[2].setIcon(num_img1[time_check % 10]);
		} else {
			count_time[0].setIcon(num_img1[time_check / 100]);
			count_time[1].setIcon(num_img1[time_check / 10]);
			count_time[2].setIcon(num_img1[time_check % 10]);
		}
	}

	int search(int x, int y, int c) { // 해당위치 탐색
		int i = 0;
		if (x < 0 || y < 0 || y > mine[0].length - 1 || x > mine.length - 1)
			return 0;
		if (mine[x][y].t == 1)
			return 0;
		if (x != 0) {
			if (mine[x - 1][y].set == true)
				++i;
			if (y != 0)
				if (mine[x - 1][y - 1].set == true)
					++i;
			if (y != mine[x].length - 1)
				if (mine[x - 1][y + 1].set == true)
					++i;
		}
		if (x != mine.length - 1) {
			if (mine[x + 1][y].set == true)
				++i;
			if (y != 0)
				if (mine[x + 1][y - 1].set == true)
					++i;
			if (y != mine[x].length - 1)
				if (mine[x + 1][y + 1].set == true)
					++i;
		}
		if (y != 0)
			if (mine[x][y - 1].set == true)
				++i;
		if (y != mine[x].length - 1)
			if (mine[x][y + 1].set == true)
				++i;
		if (i != 0) {
			if (mine[x][y].set == true)
				gameover();
			mine[x][y].bu.setIcon(num_img[i - 1]);
			mine[x][y].bu.setEnabled(false);
			mine[x][y].a = -1;
			mine[x][y].t = 1;
			clear();
		}
		if (i == 0) {
			search_around(x, y);
			clear();
		}

		return i;

	}

	void search_around(int x, int y) { // 주변탐색
		mine[x][y].bu.setEnabled(false);
		mine[x][y].a = -1;
		mine[x][y].t = 1;
		search(x - 1, y - 1, 1);
		search(x - 1, y, 1);
		search(x - 1, y + 1, 1);
		search(x, y - 1, 1);
		search(x, y + 1, 1);
		search(x + 1, y - 1, 1);
		search(x + 1, y, 1);
		search(x + 1, y + 1, 1);
	}

	void clear() { // 게임 클리어
		for (int i = 0; i < mine.length; i++)
			for (int j = 0; j < mine[i].length; j++)
				if (mine[i][j].t == 1 || mine[i][j].set == true)
					;
				else
					return;
		JOptionPane.showMessageDialog(null, "게임클리어", "성공", JOptionPane.YES_OPTION, new ImageIcon("img/icon.gif"));
		reset();

	}

	void setlocation() { // 창위치설정
		dimen = Toolkit.getDefaultToolkit().getScreenSize();
		dimen1 = frame.getSize();
		xpos = (int) ((dimen.getWidth() - dimen1.getWidth()) / 2);
		ypos = (int) ((dimen.getHeight() - dimen1.getHeight()) / 2);
	}

	void createMenu() {// 메뉴바
		JMenuBar mb = new JMenuBar();
		JMenu game = new JMenu("게임");
		JMenu help = new JMenu("도움말");
		JMenuItem[] menuItem1 = new JMenuItem[6];
		JMenuItem[] menuItem2 = new JMenuItem[2];

		String[] itemTitle1 = { "새게임", "초급", "중급", "고급", "사용자정의", "종료" };
		String[] itemTitle2 = { "도움말", "제작정보" };

		MenuActionListener listener = new MenuActionListener();
		for (int i = 0; i < menuItem1.length; i++) {
			if (i == 1 || i == 5)
				game.addSeparator();
			menuItem1[i] = new JMenuItem(itemTitle1[i]);
			menuItem1[i].addActionListener(listener);
			game.add(menuItem1[i]);
		}
		for (int i = 0; i < menuItem2.length; i++) {
			menuItem2[i] = new JMenuItem(itemTitle2[i]);
			menuItem2[i].addActionListener(listener);
			help.add(menuItem2[i]);
		}
		mb.add(game);
		mb.add(help);

		frame.setJMenuBar(mb);
	}

	class MenuActionListener implements ActionListener { // 메뉴
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch (cmd) {
			case "새게임":
				reset();
				break;
			case "초급":
				mine_num = easy;
				rows = easy;
				cols = easy;
				mine_numcheck = 0;
				reset();
				break;
			case "중급":
				mine_num = normal;
				rows = normal;
				cols = normal;
				frame.setSize(rows * mine_c.getIconWidth() + 17, 102 + mine_c.getIconHeight() * cols);
				mine_numcheck = 1;
				reset();
				break;
			case "고급":
				mine_num = hard;
				rows = hard;
				cols = hard;
				mine_numcheck = 2;
				reset();
				break;
			case "사용자정의":
				aaa = new sd();
				aaa.Frame.setVisible(true);
				frame.setVisible(false);
				break;
			case "종료":
				System.exit(0);
				break;
			}
		}

	}

	void start() {
		if (!game_start) {
			game_start = true;
			time_check = 0;
		}
	}

	class time_set extends Thread {
		public void run() {
			while (true) {
				try {
					Thread.sleep(100);
					time_check++;
					if (game_start) {
						timenum_position();
						Thread.sleep(900);
					}
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public static void main(String[] args) {
		first aa = new first();
	}
}

// 상단바 기본 cols * 18 + height = 38
// 하단 panel cols*17+35,81 + 18*rows
// frame cols*18+26,67 + 18*rows