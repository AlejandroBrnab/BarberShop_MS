package com.barbershop.apigateway.mappinglayer.reviews;

import com.barbershop.apigateway.presentationlayer.clients.ClientController;
import com.barbershop.apigateway.presentationlayer.clients.ClientResponseModel;
import com.barbershop.apigateway.presentationlayer.reviews.ReviewController;
import com.barbershop.apigateway.presentationlayer.reviews.ReviewResponseModel;
import org.mapstruct.*;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ReviewResponseMapper {

    ReviewResponseModel responseModelToResponseModel(ReviewResponseModel reviewResponseModel);

    List<ReviewResponseModel> responseModelListToResponseModelList(List<ReviewResponseModel> reviewResponseModelList);

    @AfterMapping
    default void addLinks(@MappingTarget ReviewResponseModel reviewResponseModel){
        //customer link
        Link selfLink = linkTo(methodOn(ReviewController.class).getReviewById(reviewResponseModel.getReviewId())).withSelfRel();
        reviewResponseModel.add(selfLink);

        Link allLinks = linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("all reviews");
        reviewResponseModel.add(allLinks);
    }

}
