package views;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import controllers.StatsController;
import data.TaskManager;

/**
 * The {@code StatsView} class represents the statistics dashboard view.
 * <p>
 * It displays task-related statistics such as total tasks, completed tasks, pending tasks, overdue tasks,
 * and a completion progress bar. It also contains visual analytics including:
 * <ul>
 * 	 <li>🌦 Forecast bar chart for upcoming tasks (today, tomorrow, this week, next week)</li>
 *   <li>📊 Pie chart for task distribution</li>
 *   <li>📉 Overdue history line chart (for 7/14/30 days)</li>
 *   
 * </ul>
 * Users can navigate between the charts using a set of toggle buttons.
 * 
 * <p>Designed using MVC principles where data is provided by the TaskManager (model) and visualized here.
 * JFreeChart is used for all chart-based visualizations.
 * 
 * @author Labibah Zainab
 */
public class StatsView extends JPanel{
	/**
	 *  The active chart toggle button.
	 */
	private JButton activeButton;
	
	/**
	 * Creates the default statistics view. 
	 * <p>
	 * Contains task-related statistics: total tasks, completed tasks, pending tasks, overdue tasks,
	 * and a completion progress bar.
	 * Also contains the forecast bar chart for upcoming tasks (today, tomorrow, this week, next week).
	 * 
	 * @param mainContent the panel in which the statistics view should be displayed
	 */
	public StatsView(JPanel mainContent) {
		this.setLayout(new BorderLayout(10, 10));
		
		JPanel dataPanel = new JPanel(new GridLayout(10, 1, 2, 2));
		dataPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel totaltasksLabel = new JLabel ("📋 Total tasks: " + TaskManager.getTasks().size());
		JLabel completedTodayLabel = new JLabel ("📅 Completed today: " + TaskManager.getNumTasksCompletedToday());
		JLabel completedThisWeekLabel = new JLabel ("📆 Completed this week: " + TaskManager.getNumTasksCompletedThisWeek());
		JLabel completedTasksLabel = new JLabel("✅ Total Completed: " + TaskManager.getCompletedTasks().size());
		JLabel pendingTasksLabel = new JLabel("⏳ Pending: " + TaskManager.getPendingTasks().size());
		JLabel overdueLabel = new JLabel ("❗ Overdue: " + TaskManager.getOverdueTasks().size());
		JLabel completionRate = new JLabel("📊 Completion rate: " + String.format("%.2f", TaskManager.getCompletionRate()) + "%" );
		
		for(JLabel label: new JLabel[] 
				{completionRate, completedTodayLabel, completedThisWeekLabel, completedTasksLabel, pendingTasksLabel, overdueLabel, totaltasksLabel,}) {
			label.setFont(new Font("SansSerif", Font.PLAIN, 14));
			dataPanel.add(label);
		}
		
		JPanel progressBarPanel = generateProgressBarPanel();
		
		// sits on the left side of stats view -> shows data and a progress bar
		JPanel leftStatsPanel = new JPanel(new BorderLayout(10, 10));
		leftStatsPanel.setPreferredSize(new Dimension(200, 500));
		leftStatsPanel.add(dataPanel, BorderLayout.CENTER);
		leftStatsPanel.add(progressBarPanel, BorderLayout.NORTH);
		
		// panel that hold visual data
		JPanel visualPanel = new JPanel(new BorderLayout());
		
		// default visual data is the forecast panel
		JPanel forecastPanel = generateForecastPanel();
		visualPanel.add(forecastPanel, BorderLayout.CENTER);
		
		JPanel visualNavigationPanel = generateVisualNavigationPanel(visualPanel);
		
		// sits on the right side of stats panel -> shows visual data,, and a navigation 
				// panel that toggles between different visual data
		JPanel rightStatsPanel = new JPanel(new BorderLayout(10, 10));
		rightStatsPanel.setPreferredSize(new Dimension(600, 550));
		rightStatsPanel.add(visualPanel, BorderLayout.CENTER);
		rightStatsPanel.add(visualNavigationPanel, BorderLayout.NORTH);
		
		// panel which contains the left and right stats panels
		JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		centerPanel.add(leftStatsPanel, BorderLayout.WEST);
		centerPanel.add(rightStatsPanel, BorderLayout.EAST);
		
		// contains a label, sits at the top of stats view
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel topLabel = new JLabel("Task Statistics", SwingConstants.CENTER);
		topLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		topPanel.add(topLabel);
		
		// sits at the bottom of stats view
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		buttonPanel.setPreferredSize(new Dimension(70, 80));
		BackButton backButton = new BackButton(mainContent);
		buttonPanel.add(backButton);
		
		this.add(topPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Generates a stacked bar chart showing how many tasks are due:
	 * <ul>
	 *   <li>Today</li>
	 *   <li>Tomorrow</li>
	 *   <li>This week</li>
	 *   <li>Next week</li>
	 * </ul>
	 *
	 * @return a JPanel containing the bar chart
	 */
	private JPanel generateForecastPanel() {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		int numDueToday = TaskManager.getTasksOnDate(LocalDate.now()).size();
		int numDueTomorrow = TaskManager.getTasksOnDate(LocalDate.now().plusDays(1)).size();
		int numDueThisWeek = TaskManager.getTasksDueThisWeek().size();
		int numDueNextWeek = TaskManager.getTasksDueNextWeek().size();
		
		dataset.addValue(numDueToday, "Tasks", "Today");
		dataset.addValue(numDueTomorrow, "Tasks", "Tomorrow");
		dataset.addValue(numDueThisWeek, "Tasks", "This week");
		dataset.addValue(numDueNextWeek, "Tasks", "Next week");
		
		JFreeChart forecastChart = ChartFactory.createStackedBarChart(
				"🌦 Upcoming Task Forecast",  // title
				"Time Frame",  // domain axis label
				"Number of Tasks Due",  // range axis label
				dataset,
				PlotOrientation.VERTICAL,
				false, // legend
				true, // tooltips
				false // urls
				);
		
		CategoryPlot plot = forecastChart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlineVisible(false);
		
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, new Color(0x0077b6)); // set bar color - blue
		renderer.setMaximumBarWidth(0.1); // set bar width
		
		// y axis
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // only whole numbers
		rangeAxis.setAxisLineStroke(new BasicStroke(2));
		rangeAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
		rangeAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		
		// x axis
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setAxisLineStroke(new BasicStroke(2));
		domainAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
		domainAxis.setTickLabelFont(new Font("SansSerif", Font.ITALIC, 12));
		
		
		// title appears at the top
		TextTitle title = forecastChart.getTitle();
		title.setFont(new Font("SansSerif", Font.BOLD, 16));
		title.setPaint(Color.GRAY);
		title.setPosition(RectangleEdge.TOP);
		title.setHorizontalAlignment(HorizontalAlignment.CENTER);
		title.setPadding(10, 10, 10, 10);
		
		ChartPanel forecastPanel = new ChartPanel(forecastChart);
		
		return forecastPanel;
	}
	
	/**
	 * Generates a line chart representing the number of overdue tasks over a given number of days.
	 * <p>
	 * The X-axis shows dates and the Y-axis shows overdue task counts.
	 *
	 * @param days the number of past days to display (e.g., 7, 14, or 30)
	 * @return a JPanel containing the line chart
	 */
	private JPanel generateOverdueHistoryChartPanel(int days) {
		Map<LocalDate, Integer> overdueHistory = TaskManager.getOverdueHistory(days);
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d");
		
		for(Map.Entry<LocalDate, Integer> entry: overdueHistory.entrySet()) {
			String dateLabel = entry.getKey().format(formatter);
			dataset.addValue(entry.getValue(), "Overdue Tasks", dateLabel);
		}
		
		JFreeChart overdueHistoryChart = ChartFactory.createLineChart(
				"📉 Overdue History for the last " + days + " days",  // title
				"Date",  // domain axis label
				"Number of Overdue Tasks",  // range axis label
				dataset,
				PlotOrientation.VERTICAL,
				false, // legend
				true, // tooltips
				false // urls
				);
		
		CategoryPlot plot = overdueHistoryChart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.GRAY);
		
		// adjust line color and shape
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, new Color(0xff6f61)); // peachy red
		renderer.setSeriesStroke(0, new BasicStroke(2));
		
		// y axis
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // only whole numbers
		rangeAxis.setAxisLineStroke(new BasicStroke(2));
		rangeAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
		rangeAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		
		// x axis
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setAxisLineStroke(new BasicStroke(2));
		domainAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
		domainAxis.setTickLabelFont(new Font("SansSerif", Font.ITALIC, 10));
		// add rotation so that all labels fit if a lot of labels
		if(days > 10) {
			domainAxis.setCategoryLabelPositions(
				    CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 4)
				);
		}
		
		// title appears at the top
		TextTitle title = overdueHistoryChart.getTitle();
		title.setFont(new Font("SansSerif", Font.BOLD, 16));
		title.setPaint(Color.GRAY);
		title.setPosition(RectangleEdge.TOP);
		title.setHorizontalAlignment(HorizontalAlignment.CENTER);
		title.setPadding(10, 10, 10, 10);
		
		ChartPanel overdueHistoryPanel = new ChartPanel(overdueHistoryChart);
		
		return overdueHistoryPanel;
	}
	
	/**
	 * Creates a pie chart representing the distribution of tasks by status:
	 * Completed, Pending, and Overdue.
	 * <p>
	 * The pie chart includes hover interactions (explode on hover).
	 *
	 * @return a JPanel containing the pie chart
	 */
	@SuppressWarnings("unchecked")
	private JPanel generatePieChartPanel() {
		
		DefaultPieDataset<String> dataset = new DefaultPieDataset();
		
		int numCompleted = TaskManager.getCompletedTasks().size();
		int numPending = TaskManager.getPendingTasks().size();
		int numOverdue = TaskManager.getOverdueTasks().size();
		
		dataset.setValue("Completed", numCompleted);
		dataset.setValue("Pending", numPending);
		dataset.setValue("Overdue", numOverdue);
		
		JFreeChart pieChart = ChartFactory.createPieChart(
				"📋 Task distribution", // chart title
				dataset,  // dataset
				true,     // show legend
				true,     // show tool tips
				false     // no urls
				);
		
		pieChart.setBackgroundPaint(Color.WHITE);
		
		PiePlot<String> plot = (PiePlot<String>) pieChart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlineVisible(false);
		
		plot.setSectionPaint("Completed", new Color(0x27D169)); // green
		plot.setSectionPaint("Pending", new Color(0x51aefb));  // blue
		plot.setSectionPaint("Overdue", new Color(0xfa5252));  // red
		
		//plot.setExplodePercent("Overdue", 0.10);

		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})")); // format: {0} = key, {1} = value, {2} = percent
		plot.setLabelGenerator(null); // no labels
		plot.setInteriorGap(0.04);

		
		// title appears at the bottom
		TextTitle title = pieChart.getTitle();
		title.setFont(new Font("SansSerif", Font.BOLD, 16));
		title.setPaint(Color.GRAY);
		title.setPosition(RectangleEdge.TOP);
		title.setHorizontalAlignment(HorizontalAlignment.CENTER);
		title.setPadding(10, 10, 10, 10);
		
		LegendTitle legend = pieChart.getLegend();
		legend.setPosition(RectangleEdge.RIGHT); // set position on the right side
		
		ChartPanel chartPanel = new ChartPanel(pieChart);
		chartPanel.setPreferredSize(new Dimension(500, 300));
		
		chartPanel.addChartMouseListener(new ChartMouseListener() {
			private Comparable<?> lastKey = null;
			
			@Override
			public void chartMouseMoved(ChartMouseEvent e) {
				ChartEntity entity = e.getEntity();
				
				if(entity instanceof PieSectionEntity) { // if cursor is hovering over a pie section
					
					PieSectionEntity pieEntity = (PieSectionEntity) entity;
					Comparable<?> key = pieEntity.getSectionKey();
					PiePlot plot = (PiePlot) pieChart.getPlot();
					
					// resetting previous explode
					if(lastKey != null && lastKey.equals(key)) {
						plot.setExplodePercent(lastKey, 0);
					}
					
					// explode current section
					plot.setExplodePercent(key, 0.05);
					lastKey = key;
				}
				else { // not hovering over a pie section -> reset last explode
					PiePlot plot = (PiePlot) pieChart.getPlot();
					if(lastKey != null) {
						plot.setExplodePercent(lastKey, 0);
					}
					
				}
				
			}

			@Override
			public void chartMouseClicked(ChartMouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		return chartPanel;
	}
	 
	/**
	 * Generates a task completion progress bar with a percentage indicator.
	 * The value comes from the ratio of completed tasks to total tasks represented as a percentage.
	 *
	 * @return a JPanel containing the progress bar
	 */
	private JPanel generateProgressBarPanel() {
		JPanel progressPanel = new JPanel(new BorderLayout(0, 5));
		
		JLabel label = new JLabel("Task Completion Progress", SwingConstants.CENTER);
		label.setFont(new Font("SansSerif", Font.BOLD, 14));
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue((int) TaskManager.getCompletionRate());
		progressBar.setForeground(new Color(0x27D169)); // green
		
		progressPanel.add(label, BorderLayout.NORTH);
		progressPanel.add(progressBar, BorderLayout.CENTER);
		
		return progressPanel;
	}
	
	/**
	 * Creates a navigation panel with buttons to toggle between:
	 * <ul>
	 *   <li>Task Forecast (bar chart)</li>
	 *   <li>Task Distribution (pie chart)</li>
	 *   <li>Overdue History (line chart)</li>
	 * </ul>
	 *
	 * @param visualPanel the panel where selected charts will be rendered
	 * @return a JPanel containing the navigation buttons
	 */
	private JPanel generateVisualNavigationPanel(JPanel visualPanel) {
		JPanel visualNavigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// forecast (bar chart) button
		JButton taskForecastButton = new JButton("🌦 Task Forecast");
		taskForecastButton.setFocusable(false);
		taskForecastButton.addActionListener(e -> {
			StatsController.refreshVisualPanel(visualPanel, generateForecastPanel());
			highlightActiveButton(taskForecastButton);
		});
		
		// button for task distribution pie chart
		JButton taskDistributionButton = new JButton("📋 Task Distribution");
		taskDistributionButton.setFocusable(false);
		taskDistributionButton.addActionListener(e -> {
			StatsController.refreshVisualPanel(visualPanel, generatePieChartPanel());
			highlightActiveButton(taskDistributionButton);
		});
		
		// overdue history (line chart) button
		JButton overdueHistoryButton = new JButton("📉 Overdue History");
		overdueHistoryButton.setFocusable(false);
		overdueHistoryButton.addActionListener(e -> {
			JPopupMenu overdueHistoryOptions = generatePopupMenu(visualPanel);
			overdueHistoryOptions.show(overdueHistoryButton, 0, overdueHistoryButton.getHeight());
			highlightActiveButton(overdueHistoryButton);
		});
		
		highlightActiveButton(taskForecastButton);
		
		visualNavigationPanel.add(taskForecastButton);
		visualNavigationPanel.add(taskDistributionButton);
		visualNavigationPanel.add(overdueHistoryButton);
		
		return visualNavigationPanel;
	}
	
	/**
	 * Highlights the active chart toggle button and resets the background of the previous one.
	 *
	 * @param button the JButton that was clicked to activate a visual
	 */
	private void highlightActiveButton(JButton button) {
		if(this.activeButton != null) {
			activeButton.setBackground(Color.WHITE);
		}
		activeButton = button;
		activeButton.setBackground(new Color(0xd6befa));
	}
	
	/**
	 * Creates a popup menu allowing users to select overdue history duration:
	 * last 7, 14, or 30 days.
	 *
	 * @param visualPanel the panel where the new chart should be displayed
	 * @return a JPopupMenu with options for overdue history durations
	 */
	private JPopupMenu generatePopupMenu(JPanel visualPanel) {
		JPopupMenu overdueHistoryOptions = new JPopupMenu();
		
		JMenuItem last7DaysItem = new JMenuItem("Last 7 days");
		last7DaysItem.addActionListener(e -> {
			StatsController.refreshVisualPanel(visualPanel, generateOverdueHistoryChartPanel(7));
		});
		
		JMenuItem last14DaysItem = new JMenuItem("Last 14 days");
		last14DaysItem.addActionListener(e -> {
			StatsController.refreshVisualPanel(visualPanel, generateOverdueHistoryChartPanel(14));
		});
		
		JMenuItem last30DaysItem = new JMenuItem("Last 30 days");
		last30DaysItem.addActionListener(e -> {
			StatsController.refreshVisualPanel(visualPanel, generateOverdueHistoryChartPanel(30));
		});
		
		overdueHistoryOptions.add(last7DaysItem);
		overdueHistoryOptions.add(last14DaysItem);
		overdueHistoryOptions.add(last30DaysItem);
		
		return overdueHistoryOptions;
	}

}
