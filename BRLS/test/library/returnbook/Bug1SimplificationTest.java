package library.returnbook;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap; 

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension; 

import library.entities.Book;
import library.entities.Calendar;
import library.entities.IBook;
import library.entities.IBook.BookState;
import library.entities.ICalendar;
import library.entities.ILibrary;
import library.entities.ILoan;
import library.entities.IPatron;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Patron;
import library.entities.ILoan.LoanState;
import library.entities.IPatron.PatronState;
import library.entities.helpers.BookHelper;
import library.entities.helpers.LoanHelper;
import library.entities.helpers.PatronHelper; 

@ExtendWith(MockitoExtension.class)
class Bug1SimplificationTest {

    Library library;
    ILoan loan;
    IPatron patron;
    IBook book;
    ICalendar calendar;    

    Map<Integer, IBook> catalog; 
    Map<Integer, IPatron> patrons; 
    Map<Integer, ILoan> loans; 
    Map<Integer, ILoan> currentLoans; 
    Map<Integer, IBook> damagedBooks;      
    Map<Integer, ILoan> patronLoans;

    SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
    String strdate = "11-11-2011 11:11:11";
    Date pastDueDate;
    Calendar cal;   
   
    ReturnBookControl bookControl;
    @Mock ReturnBookUI returnBookUI;   

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);  
      
        calendar = Calendar.getInstance();
        Date date = dateformat.parse(strdate);
        calendar.setDate(date);
        pastDueDate = calendar.getDate();

        catalog = new HashMap<>();
        patrons = new HashMap<>();
        loans = new HashMap<>();
        currentLoans = new HashMap<>();
        damagedBooks = new HashMap<>();
        patronLoans = new HashMap<>();

        //OK - set up an overdue loan        
        patron = new Patron("Smith", "John", "js@mail.com", 123456789, 1, 
                     0.0, PatronState.CAN_BORROW, patronLoans);
        book = new Book("Bob Ross", "Happy Little Mistakes", "001", 1, BookState.ON_LOAN);
        loan = new Loan(book, patron, pastDueDate, LoanState.OVER_DUE, 1);
        patronLoans.put(loan.getId(), loan);               

        //now set up the right system state to return the overdue loan
        library = new Library(new BookHelper(), new PatronHelper(), new LoanHelper(), 
                    catalog, patrons, loans, currentLoans, damagedBooks);
        patrons.put(patron.getId(), patron);
        catalog.put(book.getId(), book);
        loans.put(loan.getId(), loan);
        currentLoans.put(loan.getId(), loan);

        //Make sure the control class is in READY state (for book scanned)
        bookControl = new ReturnBookControl(library);
        bookControl.setUI(returnBookUI);        
    }

    //recreate the bug using code
    //1. For each of the reported bugs, produce a simplified and automated test that reliably reproduces the bug.
    @Test
    void SimplificationTest() {
        //Arrange - most of this is  done in the init function
       //Make sure the current date is 2 days after the due date
       calendar.incrementDate(2);

        //Act
        bookControl.bookScanned(book.getId());
        verify(returnBookUI).setState(IReturnBookUI.UIStateConstants.INSPECTING);
        bookControl.dischargeLoan(false);

        //Assert
        
        //I legit have no idea what the hell I am doing here. I've tried and tried but I'm so confused and I do not understand.
        //Debugger says 0, test says 2....I'm throughly confused
  //      assertTrue(loan.isDischarged());
        assertTrue(patron.isRestricted() );
        assertEquals(0, patron.getFinesPayable());
        assertFalse(patron.getFinesPayable() != 0.0);
        assertEquals(0, patron.getFinesPayable() );
    }

}