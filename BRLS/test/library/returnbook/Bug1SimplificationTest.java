package library.returnbook;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import library.entities.Book;
import library.entities.Calendar;
import library.entities.IBook;
import library.entities.ICalendar;
import library.entities.ILibrary;
import library.entities.ILoan;
import library.entities.IPatron;
import library.entities.Library;
import library.entities.Patron;
import library.entities.helpers.BookHelper;
import library.entities.helpers.CalendarFileHelper;
import library.entities.helpers.LoanHelper;
import library.entities.helpers.PatronHelper;

@ExtendWith(MockitoExtension.class)
class Bug1SimplificationTest {

    Library library;
    ILoan loan;
    IPatron patron;
    IBook book;
    ICalendar calendar;
    CalendarFileHelper calendarHelper;
    
    ReturnBookControl bookControl;
    @Mock ReturnBookUI returnBookUI;
   
    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        calendarHelper = new CalendarFileHelper();
        library = new Library(new BookHelper(), new PatronHelper(), new LoanHelper());
        bookControl = new ReturnBookControl(library);
       // returnBookUI = new ReturnBookUI(bookControl);
       // bookControl.setUI(returnBookUI);
        patron = new Patron("Smith", "John", "js@mail.com", 123456789, 1);
        book = new Book("Bob Ross", "Happy Little Mistakes", "001", 1);
        calendar = calendarHelper.loadCalendar();
    }
    
    //recreate the bug using code
    //1. For each of the reported bugs, produce a simplified and automated test that reliably reproduces the bug.
    @Test
    void SimplificationTest() {
        //Arrange
        int bookID = 1;
        loan = library.issueLoan(book, patron);
        calendar.incrementDate(3);
        
        //Act
        bookControl.bookScanned(bookID);
        bookControl.dischargeLoan(false);
        //Assert
        assertTrue(loan.isOverDue());
        assertTrue(patron.getFinesPayable() > 0.0);
    }
    

}
