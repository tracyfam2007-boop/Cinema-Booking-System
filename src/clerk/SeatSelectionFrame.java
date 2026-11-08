/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clerk;

/**
 *
 * @author TRACY
 */

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import service.SeatServiceCm;

public class SeatSelectionFrame extends JFrame{
    
    private Set<String> selectedSeats = new HashSet<>();

    public SeatSelectionFrame(String hallType,
                             String showtimeId,
                             Consumer<String> seatCallback){

        hallType = hallType.trim(); 
        int rows,cols;

        switch(hallType){
            case "IMAX":
                rows=10; 
                cols=9; 
                break;
            case "4D":
                rows=6; 
                cols=8; 
                break;
            default:
                rows=8; 
                cols=8;
        }

        setTitle("Seat Selection");
        setSize(650,550);
        setLayout(new BorderLayout());

        JPanel grid = new JPanel(new GridLayout(rows+1,cols+1,5,5));

        // GET BOOKED SEATS
        Set<String> bookedSeats = SeatServiceCm.getBookedSeats(showtimeId);
        
        for(int r=0;r<=rows;r++){
            for(int c=0;c<=cols;c++){

                if(r==0 && c==0){
                    grid.add(new JLabel(""));
                }
                else if(r==0){
                    grid.add(new JLabel(""+c,JLabel.CENTER));
                }
                else if(c==0){
                    grid.add(new JLabel(""+(char)('A'+r-1),JLabel.CENTER));
                }
                else{

                    String seat = (char)('A'+r-1)+""+c;
                    JButton btn = new JButton(seat);
                    
                    // IF BOOKED (RED + DISABLED)
                    if(bookedSeats.contains(seat)){
                        btn.setBackground(Color.RED);
                        btn.setEnabled(false);
                    }

                    btn.addActionListener(e->{

                        if(selectedSeats.contains(seat)){
                            selectedSeats.remove(seat);
                            btn.setBackground(null);
                        }else{
                            selectedSeats.add(seat);
                            btn.setBackground(Color.GREEN);
                        }
                    });

                    grid.add(btn);
                }
            }
        }

        JButton doneBtn = new JButton("Done");

        doneBtn.addActionListener(e->{
            if(selectedSeats.isEmpty()){
                JOptionPane.showMessageDialog(this,"Select at least one seat");
                return;
            }

            String result = String.join(",", selectedSeats);
            seatCallback.accept(result);
            dispose();
        });

        add(grid,BorderLayout.CENTER);
        add(doneBtn,BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
