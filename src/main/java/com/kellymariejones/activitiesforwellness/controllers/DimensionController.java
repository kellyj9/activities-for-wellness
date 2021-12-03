package com.kellymariejones.activitiesforwellness.controllers;

import com.kellymariejones.activitiesforwellness.data.ActivityRepository;
import com.kellymariejones.activitiesforwellness.data.DimensionRepository;
import com.kellymariejones.activitiesforwellness.data.SampleRepository;
import com.kellymariejones.activitiesforwellness.models.Dimension;
import com.kellymariejones.activitiesforwellness.models.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("dimension")
public class DimensionController {

     // Autowired annotation specifies that Spring Boot should auto-populate this field
    // feature of Spring Boot - dependency injection / inversion of control
     @Autowired
    private DimensionRepository dimensionRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SampleRepository sampleRepository;

    @GetMapping
    public String displayDimensionList(Model model) {

        // get the list of dimensions
        Iterable<Dimension> result = new ArrayList<>();
        result = dimensionRepository.findAll();

        // Note: The following code populates the dimension table if it's empty
        // check if the dimension table is populated
        if (!result.iterator().hasNext()) {
            // POPULATES dimension table with data
            // Note:  This is only used for setup of the application.
            // Activities are associated with a dimension.
            // Any records in the activity table should also get deleted!
            // Samples are associated with a dimension.
            // Any records in the sample table should also get deleted!

            // Populate the 8 dimensions
            Dimension dimension1 = new Dimension ("Emotional",
            "Emotional wellness involves developing an ability to cope " +
                    "effectively with life and managing your emotions in a constructive " +
                    "way.");
            Dimension dimension2 = new Dimension ("Spiritual",
                    "Spiritual wellness is related to your values and beliefs that " +
                            "help you find meaning and purpose in your life.");
            Dimension dimension3 = new Dimension ("Intellectual",
            "Intellectual wellness is when you recognize your unique talents " +
                    "to be creative and you find ways to expand your knowledge and skills.");
            Dimension dimension4 = new Dimension ("Physical",
                    "Physical wellness is affected by activities such as exercise, " +
                            "healthy nutrition, and adequate sleep.");
            Dimension dimension5 = new Dimension ("Environmental",
            "Environmental wellness is related to the surroundings you occupy.");
            Dimension dimension6 = new Dimension ("Financial",
            "Financial wellness is a feeling of satisfaction about your financial " +
                    "situation.");
            Dimension dimension7 = new Dimension ("Occupational",
            "Occupational wellness is a sense of personal satisfaction with the " +
                    "work you choose.");
            Dimension dimension8 = new Dimension ("Social",
            "Social wellness is developing a sense of connectedness " +
                    "and belonging.");
             dimensionRepository.save(dimension1);
            dimensionRepository.save(dimension2);
             dimensionRepository.save(dimension3);
            dimensionRepository.save(dimension4);
             dimensionRepository.save(dimension5);
            dimensionRepository.save(dimension6);
            dimensionRepository.save(dimension7);
            dimensionRepository.save(dimension8);

            activityRepository.deleteAll();
            sampleRepository.deleteAll();

            // Next, populate the Sample table with sample activities.  Samples are
            // related to dimensions in a Many to One relationship.

           Sample sample1 = new Sample(
                        "Write about my feelings and thoughts in a journal.");
           sample1.setDimension(dimension1);
                sampleRepository.save(sample1);
            Sample sample2 = new Sample(
                    "Try yoga, breathing exercises, or meditation to " +
                            "remain calm and centered.");
            sample2.setDimension(dimension1);
            sampleRepository.save(sample2);

            Sample sample20 = new Sample(
                    "Try yoga, breathing exercises, or meditation to " +
                            "remain calm and centered.");
            sample20.setDimension(dimension2);
            sampleRepository.save(sample20);



            // now refresh the result of the findAll()
            result = dimensionRepository.findAll();
        }

        // add the title of the page to the model
        model.addAttribute("title", "Dimensions of Wellness");

        // add all dimensions in the dimensionRepository to the model
        model.addAttribute("dimension", result);
        return "dimension/index";
    }
}