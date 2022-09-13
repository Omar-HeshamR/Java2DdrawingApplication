
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import java.awt.Font;
import java.awt.event.MouseListener;


/**
 * @author Omar
 */

public class DrawingApplicationFrame extends JFrame implements ActionListener
{
    // JPanel Varibles
    private final JPanel ButtonLine1;
    private final JPanel ButtonLine2;
    private final JPanel ButtonPanel;
    private final DrawPanel mainPanel;
    private final JPanel locationPanel;
    
    // ButtonLine1 Varibles
    private final JLabel shapeText;
    private final JComboBox<String> shapePicker;
    private final String[] shapes = {
        "line","oval","rectangle"
    };
    private final JButton colorPicker1;
    private final JButton colorPicker2;
    private final JColorChooser color1chooser;
    private final JColorChooser color2chooser;
    private final JButton undoButton;
    private final JButton clearButton;

    //ButtonLine2 Varibles
    private final JLabel options;
    private final JCheckBox filled;
    private final JCheckBox useGradient;
    private final JCheckBox dashed;
    private final JLabel lineWidthText;
    private final JSpinner lineWidth;
    private final JLabel dashLengthText;
    private final JSpinner dashLength;
    
    // OTHER Varibles
    public ArrayList<MyShapes> shapesToDraw;
    private final JLabel locationText;
    private String currentShapeName;
    private Color color1;
    private Color color2;
    private int lineWidthValue;
    private float dashLengthValue;
    private boolean isDashed = false;
    private boolean isFilled = false;
    private boolean isUseGradient = false;
    private BasicStroke stroke;
    private Paint paint;
    
    public DrawingApplicationFrame()
    {   
        setLayout(new BorderLayout());
                
        // intilaization
        ButtonLine1 = new JPanel();
        ButtonLine2 = new JPanel();
        ButtonPanel = new JPanel();
        locationPanel = new JPanel();
        mainPanel = new DrawPanel();
        shapeText = new JLabel("Shape:");
        shapePicker = new JComboBox<String>(shapes);
        colorPicker1 = new JButton("1st Color");
        colorPicker2 = new JButton("2nd Color");
        color1chooser = new JColorChooser();
        color2chooser = new JColorChooser();
        undoButton = new JButton("Undo");
        clearButton = new JButton("Clear");
        options = new JLabel("Options: ");
        filled = new JCheckBox("Filled");
        useGradient = new JCheckBox("Use Gradient");
        dashed = new JCheckBox("Dashed");
        lineWidthText = new JLabel("Line Width:");
        lineWidth = new JSpinner();
        lineWidth.setPreferredSize(new Dimension(35, 25));
        lineWidth.setValue(10);
        dashLengthText = new JLabel("Dash Length:");
        dashLength = new JSpinner();
        dashLength.setPreferredSize(new Dimension(35, 25));
        dashLength.setValue(15);
        locationText = new JLabel();
        shapesToDraw = new ArrayList<MyShapes>();
        
        //ButtonLine1Functionalties
        colorPicker1.addActionListener(e -> { 
            color1 = JColorChooser.showDialog(null,"Choose Color One", Color.BLACK);
        });
         colorPicker2.addActionListener(e -> { 
            color2 = JColorChooser.showDialog(null,"Choose Color One", Color.BLACK);
        });
        clearButton.addActionListener(e -> { 
            mainPanel.clearPaint();    
        });
         undoButton.addActionListener(e -> { 
            mainPanel.Undo();    
        });
        
        
        //addButtonLine1
        ButtonLine1.setBackground(Color.CYAN);
        ButtonLine1.add(shapeText);
        ButtonLine1.add(shapePicker);
        ButtonLine1.add(colorPicker1);
        ButtonLine1.add(colorPicker2);
        ButtonLine1.add(undoButton);
        ButtonLine1.add(clearButton);

        //addButtonLine2
        ButtonLine2.setBackground(Color.CYAN);
        ButtonLine2.add(options);
        ButtonLine2.add(filled);
        ButtonLine2.add(useGradient);
        ButtonLine2.add(dashed);
        ButtonLine2.add(lineWidthText);
        ButtonLine2.add(lineWidth);
        ButtonLine2.add(dashLengthText);
        ButtonLine2.add(dashLength);
        
        //ButtonLine2 Functionalties
        filled.addActionListener(e -> { 
            if(filled.isSelected())
                isFilled = true;
            else
		isFilled = false;    
        });
        useGradient.addActionListener(e -> { 
            if(useGradient.isSelected())
                isUseGradient = true;
            else
		isUseGradient = false;    
        });
        dashed.addActionListener(e -> { 
            if(dashed.isSelected())
                isDashed = true;
            else
		isDashed = false;    
        });
        
        //ButtonPanel
        ButtonPanel.setLayout(new GridLayout(2,1));
        ButtonPanel.setBackground(Color.CYAN);
        ButtonPanel.add(ButtonLine1);
        ButtonPanel.add(ButtonLine2);
        
        //locationPanel
        locationText.setFont(new Font("Defualt", Font.BOLD, 10));
        locationPanel.setBackground(Color.LIGHT_GRAY);
        locationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        locationPanel.add(locationText);
        mainPanel.addMouseListener(new MyMouseAdapter());
        mainPanel.addMouseMotionListener(new MyMouseAdapter());
        
        //Main Drawing Panel
        
        // add them
        add(ButtonPanel, BorderLayout.NORTH);
        add(mainPanel,BorderLayout.CENTER);
        add(locationPanel,BorderLayout.SOUTH);
    }

    private class MyMouseAdapter extends MouseAdapter implements MouseListener{
        @Override
        public void mouseMoved(MouseEvent event) {
            // get Point location and turn into a String
            String location = String.format("(%d, %d)", event.getX(), event.getY());
            locationText.setText(location);
        }
        
       public void mousePressed(MouseEvent event){  
            Point pnt1 = event.getPoint();   
            Point pnt2 = new Point((int) pnt1.getX() + 10, (int) pnt1.getY() + 10); 
            
            lineWidthValue = (Integer) lineWidth.getValue();
            dashLengthValue = (Integer) dashLength.getValue();
        
            // To see gradient        
            if (isUseGradient){
                paint = new GradientPaint(0, 0, color1, 50, 50, color2, true);
            }else{
                paint = color1;
            }
            
            // To see if Dashed
            if (isDashed)
            {
                stroke = new BasicStroke(lineWidthValue, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, new float[]{dashLengthValue}, 0);
            } else
            {
                stroke = new BasicStroke(lineWidthValue, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            }      
    
           currentShapeName = shapePicker.getSelectedItem().toString();
           MyShapes currentShape;
           
            switch(currentShapeName) 
                {                    
                    case "line":
                        currentShape = new MyLine(pnt1, pnt1, paint, stroke);
                        break;
                    case "rectangle":
                        currentShape = new MyRectangle(pnt1, pnt1, paint, stroke, isFilled);
                        break;                        
                    case "oval":
                        currentShape = new MyOval(pnt1, pnt1, paint, stroke, isFilled);
                        break;          
                    default: 
                        currentShape = new MyLine(pnt1, pnt1, paint, stroke); 
                }
            
            shapesToDraw.add(currentShape);
            repaint();
       }
       
       public void mouseReleased(MouseEvent event){
            repaint();
        }
       
       public void mouseDragged(MouseEvent event){
            int lastIndex = shapesToDraw.size() - 1;
            if (lastIndex >= 0) {
                    MyShapes currentShape = shapesToDraw.get(lastIndex);
            if (currentShapeName == "line") {
                    ((MyLine) currentShape).setEndPoint(event.getPoint());
            }else {
                   ((MyBoundedShapes) currentShape).setEndPoint(event.getPoint());
              }
                }
                repaint();
        }
    }

    public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub	
	}
    
    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {

        public DrawPanel()
        {
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;  
                                      
            if (shapesToDraw == null){}
            else{ for (int i = 0; i < shapesToDraw.size(); i++) {
                shapesToDraw.get(i).draw(g2d);
            } }
          g2d.dispose();
        }
        
        public void clearPaint(){
            shapesToDraw.clear();
            repaint(); 
        }
        public void Undo(){
            if (shapesToDraw.size() > 0){
            int lastIndex = shapesToDraw.size() - 1;
            shapesToDraw.remove(lastIndex);
            }
            repaint(); 
        }
       }
    }

