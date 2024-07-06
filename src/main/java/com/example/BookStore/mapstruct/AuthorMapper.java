//package com.example.BookStore.mapstruct;
//
//import com.example.BookStore.DTO.AuthorDTO;
//import com.example.BookStore.DTO.BookDTO;
//import com.example.BookStore.model.Author;
//import com.example.BookStore.model.Book;
//import com.example.BookStore.repository.BookRepository;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//import java.util.List;
//
//@Mapper
//public interface AuthorMapper {
//    BookMapper bookMapper = BookMapper.INSTANCE;
//
//    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);
//
//    @Mapping(source = "books", target = "books", ignore = true)
//    AuthorDTO toDTO(Author author);
//
//    @Mapping(source = "books", target = "books", ignore = true)
//    Author toEntity(AuthorDTO authorDTO);
//
//    //- Convert Author -> AuthorDTO
//    //! Dung khi RESPONSE
//    default AuthorDTO toDTOWithBooks(Author author) {
//        AuthorDTO authorDTO = toDTO(author);
//
//        List<BookDTO> bookDTOs = bookMapper.toListDTO(author.getBooks());
//        authorDTO.setBooks(bookDTOs);
//
//        return authorDTO;
//    }
//
//    //- Convert AuthorDTO -> Author
//    //! Dùng khi REQUEST
//    default Author toEntityWithBooks(AuthorDTO authorDTO, BookRepository bookRepository) {
//        Author author = toEntity(authorDTO);
//
//        List<Book> books = bookMapper.toListEntity(authorDTO.getBooks(), bookRepository);
//
//        for (Book book : books) {
//            if (!book.getAuthors().contains(author)) {
//                book.getAuthors().add(author);
//            }
//        }
//        author.setBooks(books);
//
//        return author;
//    }
//}
