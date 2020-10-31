package library.returnbook;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import library.entities.Book;
import library.entities.Calendar;
import library.entities.IBook;
import library.entities.ILibrary;
import library.entities.ILoan;
import library.entities.IPatron;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Patron;
import library.entities.IBook.BookState;
import library.entities.ICalendar;
import library.entities.ILoan.LoanState;
import library.entities.IPatron.PatronState;
import library.entities.helpers.IBookHelper;
import library.entities.helpers.ILoanHelper;
import library.entities.helpers.IPatronHelper;

@ExtendWith(MockitoExtension.class)
class Bug2SimplificationTest {

    //There is absolutely nothing here because I don't understand how to make the simplification tests work
    
    ReturnBookControl bookControl;
    Book book;
    Patron patron;
    Loan loan;
    ILibrary library;
    IBookHelper bookHelper;
    IPatronHelper patronHelper;
    ILoanHelper loanHelper;
    Map<Integer, IBook> catalog;
    Map<Integer, IPatron> patronsList;
    Map<Integer, ILoan> loanList;
    Map<Integer, ILoan> currentLoansList;
    Map<Integer, IBook> damagedBooksList;
    Map<Integer, ILoan> patronLoans;
    
    Date pastDueDate;
    ICalendar cal;
    SimpleDateFormat dateformat;
    String strdate;
    
    @Mock ReturnBookUI returnBookUI;
    
    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        
        patron = new Patron("Smith", "John", "js@mail.com", 123456789, 1, 
                0.0, PatronState.CAN_BORROW, patronLoans);
        book = new Book("Bob Ross", "Happy Little Mistakes", "001", 1, BookState.ON_LOAN);
        
        patronLoans = new HashMap<>();
        catalog = new HashMap<>();
        
        patronsList = new HashMap<>();
        loanList = new HashMap<>();
        currentLoansList = new HashMap<>();
        damagedBooksList = new HashMap<>();
        
        dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        strdate = "11-11-2011 11:11:11";
        cal = Calendar.getInstance();
        Date date;
        
        //Apparently it complained about this so to make it stop complaining I threw in the try/catch
        try {
            date = dateformat.parse(strdate);
            cal.setDate(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        pastDueDate = cal.getDate();
        
        loan = new Loan(book, patron, pastDueDate, LoanState.OVER_DUE, 1);
        patronLoans.put(loan.getId(), loan); 
        
        
        library = new Library(bookHelper, patronHelper, loanHelper, catalog, patronsList,
                loanList,currentLoansList, damagedBooksList);
        bookControl = new ReturnBookControl(library);
        bookControl.setUI(returnBookUI);  
    }
    
    
    @Test
    void SimplificationTest() {
        //Arrange
        cal.incrementDate(2);
        double expected = 2.0;
        //Act
        bookControl.bookScanned(book.getId());
        //Assert
        assertEquals(expected, patron.getFinesPayable());
    }
    
    
    
    
    
    
    
 /*   
    @Test
    void defaultTest() {
        fail("Not yet implemented");
    }
*/
}
