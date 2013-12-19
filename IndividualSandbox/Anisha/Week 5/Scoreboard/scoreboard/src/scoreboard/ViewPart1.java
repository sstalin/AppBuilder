package scoreboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

public class ViewPart1 extends ViewPart {

	public ViewPart1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		Table table = new Table(parent, SWT.SINGLE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn column1 = new TableColumn(table, SWT.LEFT);
		TableColumn column2 = new TableColumn(table, SWT.LEFT);
		TableItem row1 = new TableItem(table, SWT.NONE);
		TableItem row2 = new TableItem(table, SWT.NONE);

		column1.setWidth(100);
		column1.setText("Home");
		column2.setWidth(100);
		column2.setText("Visitor");

		row1.setText(new String[] { "7", "3" });
		row2.setText(new String[] { "5", "11" });
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
