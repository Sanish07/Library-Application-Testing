package com.sessions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LibraryTest {

    Library library;
    @BeforeEach
    public void object_setup(){
        library = new Library();
        System.out.println("At BeforeEach Method");
    }

    @AfterEach
    public void afterTest(){
        System.out.println("AfterEach Method");
    }

    @BeforeAll
    public static void beforeAll(){
        System.out.println("Before All Testcases have started");
    }

    @AfterAll
    public static void afterAll(){
        System.out.println("After all testcases have completed.");
    }

    @Test
    public void the_default_number_of_books_should_be_one_initially(){ //Constructor Library() test
        int totalNumberOfBooks = library.getBooks().size();
        assertEquals(1, totalNumberOfBooks); //Ensuring total books are 1 initially
    }


    @Test
    public void adding_to_catalogue_should_increase_books_size_and_newly_created_book_id_should_be_2(){ //addToCatalogue() method test
        Book newlyCreatedBook = library.addToCatalogue("Gulliver's Travels","Jonathan Swift",300,15.95);

        int totalBooks = library.getBooks().size();
        List<Book> availableBooks = library.getBooks();

        assertEquals(2, newlyCreatedBook.getId()); //Ensuring new book ID is 2
        assertThat(totalBooks, equalTo(2)); // Ensuring total number of books are 2
        assertThat(availableBooks,hasItem(newlyCreatedBook)); // Ensuring that the catalogue list has newly added book.
    }

    @Test
    public void findBookByName_should_return_book_object_when_called_with_book_available_in_the_library(){ //findBookByName() method test - Pass Case Scenario
        Book book = library.findBookByName("The God Of Small Things");
        assertNotNull(book);
    }

    @Test
    public void findBookByName_should_return_null_when_called_with_non_existent_book_in_library(){ //findBookByName() method test - Fail Case Scenario
        Book book = library.findBookByName("The Witcher");
        assertNull(book);
    }

    @Test
    public void calculateBookRent_should_return_2_when_no_of_days_are_4(){
        //We will use mocking here since the rentedDate variable has private access in RentedBook class and modifying it for testing is not possible due to it.

        RentedBook rentedBook = Mockito.mock(RentedBook.class); //Mocking RentedBook class
        LocalDate fourDaysBeforeToday = LocalDate.now().minusDays(4);

        Mockito.when(rentedBook.getRentedDate()).thenReturn(fourDaysBeforeToday);
        //Above method indicates that whenever getRentedDate() method will be called in this test function it will return the date four days before now

        Double calculateRentPrice = library.calculateBookRent(rentedBook);
        assertThat(calculateRentPrice, equalTo(2.0));

        Mockito.verify(rentedBook,Mockito.times(2)).getRentedDate();
        //Mockito.verify method (Also an assertion) ensures that the nested method (getRentedDate in this case)
        // has been called the number of times in its 2nd param by the object in 1st param
        // in the main program
    }

    @Test
    public void calculateBookRent_should_return_6_when_no_of_days_are_6() {
        RentedBook rentedBook = Mockito.mock(RentedBook.class);
        LocalDate sixDaysBeforeNow = LocalDate.now().minusDays(6);

        Mockito.when(rentedBook.getRentedDate()).thenReturn(sixDaysBeforeNow);
        Double calculateRentPrice = library.calculateBookRent(rentedBook);
        assertThat(calculateRentPrice,equalTo(6.0));
        Mockito.verify(rentedBook,Mockito.times(2)).getRentedDate();
    }

    //Test-Driven Development approach below - Write failing test case and then
    // implement the functionality in main application
    @Test
    public void when_returning_book_receipt_should_be_returned(){
        RentedBook rentedBook = library.rent("The God Of Small Things");
        Double amount = 5.0;
        Receipt bookReceipt = library.returnBook(rentedBook, amount);
        assertNotNull(bookReceipt);
        assertThat(bookReceipt.bookName, equalTo("The God Of Small Things"));
        assertThat(bookReceipt.receiptDate, equalTo(LocalDate.now()));
        assertThat(bookReceipt.amountGiven, equalTo(amount));
    }


}