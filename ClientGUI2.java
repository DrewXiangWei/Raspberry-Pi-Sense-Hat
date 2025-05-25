package client_server2; // create name space that organize a set related classes & interfaces

import java.net.InetAddress; // import the class which is include in Java input 
import java.net.Socket; // import classes for create and manage GUI layout
import java.awt.BorderLayout; // layout manage to arrange components
import java.awt.Color; // import for setting colors
import java.awt.Dimension; // import for specify the size
import java.awt.event.ActionEvent; // import to represent action event
import java.awt.event.ActionListener; // import to handle action
import java.awt.event.ItemEvent; // used to represents item state changes
import java.awt.event.ItemListener; // used to handle item events
import java.io.*; // import classes for input/output operations and provide class for readings/writing data
import java.util.ArrayList; // use to resize the array
import java.util.List; // use for list data structures
import java.util.Scanner; // use for reading input, from console

import javax.swing.ButtonGroup;	// set a group of buttons
import javax.swing.JButton; // create clickable buttons in GUI
import javax.swing.JFrame; // create frame for GUI application
import javax.swing.JLabel; // display text in GUI
import javax.swing.JPanel; // set container that group up for better layout
import javax.swing.JRadioButton; // set button that can be select
import javax.swing.JToggleButton; // create button that toggles between two states

// define ClientGUI2 class that import ActionListener actions
// so class will handle action events
public class ClientGUI2 implements ActionListener {

	JFrame window = new JFrame("Event listener"); // create main window with Jframe

	JPanel panel = new JPanel(); // create panel to hold components
	JButton buttonAverage = new JButton("Get Average"); // create button to get average
	JButton buttonMax = new JButton("Get Maximum"); // create button to get maximum
	JButton buttonMin = new JButton("Get Minimum"); // create button to get minimum
	JLabel label = new JLabel("No button Pressed"); // display selection status for new code run
    // New for ToggleButton
    JToggleButton toggleButton = new JToggleButton("Temperature"); // create button to start collect temperature 
    String menu = "1"; // set string to menu and 1
    //end New


	public ClientGUI2() {

		buttonAverage.setPreferredSize(new Dimension(300, 100)); // set the size for average button
		buttonMin.setPreferredSize(new Dimension(300, 100)); // set the size for minimum button
		buttonMax.setPreferredSize(new Dimension(300, 100)); // set the size for maximum button
		label.setPreferredSize(new Dimension(200, 100)); // set preferred size for the label to control its display size
		
		buttonAverage.addActionListener(this); // set action to average button and respond clicked button
		buttonMin.addActionListener(this); // set action to minimum button and respond clicked button
		buttonMax.addActionListener(this); // set action to maximum button and respond clicked button
		
	    //New for ToggleButton
		toggleButton.setLabel("Temperature"); // set a label on the button 
        toggleButton.addItemListener(itemListener); // add item Listener for toggle state changes
        //end New

		panel.add(buttonAverage); // add label on the average button
		panel.add(buttonMin); // add label on the minimum button
		panel.add(buttonMax); // add label on the maximum button
		panel.add(label);
		
        window.add(toggleButton, BorderLayout.NORTH); //set the button on the top of the window 
		window.getContentPane().add(panel); //add label into the panel
		window.setSize(500, 700); //set the size for window page
		window.pack(); //adjust components to fit the window
		window.setVisible(true); //make window visible
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //set exit as close running
	}

	public static void main(String[] args) throws Exception, InterruptedException { 
    //main method serves as the entry point of the program
    //make an instance of the ClientGUI2 class, initializing the GUI

		ClientGUI2 client = new ClientGUI2();

	}
    //New for handling toggle button
	ItemListener itemListener = new ItemListener() { //define an item to respond to state changes in button
		 

        public void itemStateChanged(ItemEvent itemEvent)
        {

            // event is generated in button
            int state = itemEvent.getStateChange();

            // if selected set value for temperature
            if (state == ItemEvent.SELECTED) {
            	toggleButton.setLabel("Temperature");
            	menu = "1";
                //Optional print to console
                System.out.println("Temperature");
                
            } // else set value for humidity
            else {
            	toggleButton.setBackground(Color.red);
            	toggleButton.setLabel("Humidity");
                menu = "2";
                //Optional print to console
                System.out.println("Humidity");
            }
        }
    };
    // end New
	
	@Override
	public void actionPerformed(ActionEvent e ) {

		// When button pressed, get value from server according to button pressed
		// "1" means getAverage
		// "2" means getMaximum
		// "3" means getMinimum ... not coded yet
		

		if (e.getSource().equals(buttonAverage)) { //check if average button clicked

			try {
				label.setText("Average button was pressed"); //update the label
				System.out.println("In Try"); //print out message when processing
				
				
				Socket s = connectToServer(); //connect to server and get socket
				String[] data = correspondWithServer(s, menu); //send the menu selection and receive data from server

				
				double average = getAvg(data); //calculate average
				if (average < 5) { //if average less than 5
					panel.setBackground(Color.blue); //set blue colors background
				} else if (average > 30) { //if average more than 30
					panel.setBackground(Color.red);
				} else if ((average > 5) & (average < 30)) { //if average between 5&30
					panel.setBackground(Color.green);
				}
				System.out.println("Average:"+ average); //print average value


			} catch (IOException | InterruptedException e1) { // catch any thread interuption
				e1.printStackTrace(); // print stack trace for debug
			}

		} else if (e.getSource().equals(buttonMax)) { // check if maximum button clicked
			label.setText("Maximum button was pressed"); // display maximum button was clicked

			try {
				Socket s = connectToServer(); // connect to server and get socket
				String[] data = correspondWithServer(s, menu); // send menu selection and receive data
				double max = getMax(data); // calculate max value from received data
				if (max < 5) {
					panel.setBackground(Color.blue);
				} else if (max > 30) {
					panel.setBackground(Color.red);
				} else if ((max > 5) & (max < 30)) {
					panel.setBackground(Color.green);
				}
				System.out.println("Maximum: "+ max);
				//end replace

			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(buttonMin)) {
			// Replace the following code with code for Minimum
			label.setText("Minimum button was pressed");
			
			try {
				Socket s = connectToServer(); // connect to server and get socket
				String[] data = correspondWithServer(s, menu); // send menu selection
				double min = getMin(data);
				if (min < 5) {
					panel.setBackground(Color.blue);
				} else if (min > 30) {
					panel.setBackground(Color.red);
				} else if ((min > 5) & (min < 30)) {
					panel.setBackground(Color.green);
				}
				System.out.println("Minimum:" + min);

			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace(); // debug for stack trace
			}
		}
	}

	public static Socket connectToServer() throws IOException, InterruptedException {
		// Use ipconfig (Windows) or ifconfig(Linux) to find the IP address
		// Change the IP address to that of your computer.
		InetAddress inet = InetAddress.getByName("192.168.20.108");// set up address of server
		// create socket using server ip address and port....
		Socket s = new Socket(inet, 2003);
		// wait patiently....
		Thread.sleep(200);
		// input from the socket (s)
		return s;

	}

    // Method to communicate with the server
	public static String[] correspondWithServer(Socket s, String menu) throws IOException {
		// String menu = "1";
		DataOutputStream writeToServer = new DataOutputStream(s.getOutputStream());
		writeToServer.writeBytes(menu);
		// input from the socket (s)
		InputStream in = s.getInputStream();
		// scanner object to access the input
		Scanner scanner = new Scanner(in);
		// inputLine is the next (only) line in our "document"
		String inputLine = scanner.nextLine();

		System.out.println(inputLine);// print entire line

		String[] data = inputLine.split(" ");// create array that splits on the space

		disconnectFromServer(scanner,s);  // disconnect Socket and Scanner
		return data;
	}

    // Method to calculate average of received data
	public static double getAvg(String[] data) {
		double total = 0.0;
		for (String item : data) {
			total += Double.valueOf(item); // Convert each item to a double and add to total
		}
		return total / data.length; // Return the average (total divided by number of items)

	}

    // Method to get the minimum value from the data
	public static double getMin(String[] data) {
		double min=900; // Start with a larger value
		for (String item : data) {
			double value = Double.valueOf(item); // Convert the item to double
			if(value < min) // If this value is smaller than the current min{
				min = value; // Set min to the smaller value
			}
		}
		return min; // Return the minimum value
	}

    // Method to get the maximum value from the data
	public static double getMax(String[] data) {
		double max = 0; // Start with 0 as initial max value
		for (String item : data) {
			double value = Double.valueOf(item); // Convert the item to double
			if(value > max) // If this value is larger than the current max{
				max = value; // Set max to the larger value
			}
		}
		return max; // Return the maximum value
	}

    // Method to close connections
	public static void disconnectFromServer(Scanner scanner, Socket s) throws IOException {
		scanner.close(); // Close the scanner object
		s.close(); // Close the socket connection to the server
	}
}