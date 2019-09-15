package com.cs4743;

public class Book {

    // book object
    public Book(){
        title = this.getTitle();
        summary = this.getSummary();
        pubYear = this.getPubYear();
        isbn = this.getIsbn();
    }

    public String title = "";
    public String summary = "";
    public int pubYear = 0;
    public String isbn = "";

    // creates a book object based on the given bookListIndex.
    public Book buildBook(BookListIndex bookListIndex){
        switch(bookListIndex){
            case BOOK1:
                this.setTitle("The Giving Tree");
                this.setSummary("The book follows the lives of a female apple tree and a boy, who develop a relationship with one another. The tree is very \"giving\" and the boy evolves into a \"taking\" teenager, man, then elderly man. Despite the fact that the boy ages in the story, the tree addresses the boy as \"Boy\" his entire life.");
                this.setPubYear(1964);
                this.setIsbn("9780060256654");
                break;
            case BOOK2:
                this.setTitle("Of Mice and Men");
                this.setSummary("Two displaced migrant ranch workers, who move from place to place in California in search of new job opportunities during the Great Depression in the United States");
                this.setPubYear(1937);
                this.setIsbn("9780140177398");
                break;
            case BOOK3:
                this.setTitle("Lord of the Flies");
                this.setSummary("William Golding's 1954 novel Lord of the Flies tells the story of a group of young boys who find themselves alone on a deserted island. They develop rules and a system of organization, but without any adults to serve as a 'civilizing' impulse, the children eventually become violent and brutal.");
                this.setPubYear(1954);
                this.setIsbn("9780399501487");
                break;
            case BOOK4:
                this.setTitle("To Kill a Mockingbird");
                this.setSummary("To Kill a Mockingbird is the story of a young girl named Scout whose father is a lawyer defending a black man accused of raping a white woman. She lives in a small Southern town that is shaken by the trial, because the man could not have physically committed the crime.");
                this.setPubYear(1960);
                this.setIsbn("9780060935467");
                break;
            case BOOK5:
                this.setTitle("The Great Gatsby");
                this.setSummary("Set in the Roaring Twenties of the USA, it is a novel about the young and mysterious millionaire Jay Gatsby and his quixotic obsession for the beautiful former debutante Daisy Buchanan and explores themes of decadence,idealism, resistance to change, social upheaval, and excesses of that era of the American Dream.");
                this.setPubYear(1925);
                this.setIsbn("9780743273565");
                break;
            default:
                System.out.println("Unable to build book.");
                break;
        }
        return this;
    }

    // getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getPubYear() {
        return pubYear;
    }

    public void setPubYear(int pubYear) {
        this.pubYear = pubYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
