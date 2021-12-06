package com.kellymariejones.activitiesforwellness.controllers;

import com.kellymariejones.activitiesforwellness.data.DimensionRepository;
import com.kellymariejones.activitiesforwellness.data.SampleRepository;
import com.kellymariejones.activitiesforwellness.models.Dimension;
import com.kellymariejones.activitiesforwellness.models.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private DimensionRepository dimensionRepository;

    @Autowired
    private SampleRepository sampleRepository;

    @GetMapping
    public String getIndex (Model model) {
        // set the title of the home page
        model.addAttribute("title", "Welcome");

        // get the list of dimensions
        Iterable<Dimension> result = dimensionRepository.findAll();

        // Note: The following code populates the static dimensions list if empty
        // and populates the static sample activities list for each dimension

        // START - sample and dimension population
        // check if the dimension table is populated
        if (!result.iterator().hasNext()) {
            // POPULATE dimension table with data
            // Note:  This is only used for setup of the application.
            // Caution:
            // Activities are associated with a dimension.
            // All records in the activity table must also be deleted first!!!
            // Samples are associated with a dimension.
            // All records in the sample table must also be deleted first!!!

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

            // Next, populate the activity samples.  Samples are
            // related to dimensions in a Many to One relationship.

            // Emotional
            Sample sample1 = new Sample(
                    "Write about my feelings and thoughts in a journal.");

            Sample sample2 = new Sample(
                    "Try yoga, breathing exercises, or meditation to " +
                            "remain calm and centered.");

            Sample sample3 = new Sample(
                    "Play some music to suit my mood.");

            // Spiritual
            Sample sample11 = new Sample(
                    "Watch or listen to a guided meditation.  " +
                            "I could search for these on YouTube.");

            Sample sample12 = new Sample(
                    "Learn about different organizations or groups in " +
                            "my community and decide which ones are the " +
                            "best fit for me.");

            Sample sample13 = new Sample(
                    "Take time to appreciate the beauty of " +
                            "nature, my life, and my world.");

            // Intellectual
            Sample sample21 = new Sample(
                    "Find a new book.  I can become a member at my local " +
                            "library to access books, other materials,  and events.");

            Sample sample22 = new Sample(
                    "Look up local college websites and see what classes " +
                            "or workshops they offer.");

            Sample sample23 = new Sample(
                    "Watch an inspirational talk on YouTube.  " +
                            "For example, a 'Ted Talk.'");

            // Physical
            Sample sample31 = new Sample(
                    "Join or visit a local gym, YMCA, fitness club, " +
                            "or neighborhood pool.");

            Sample sample32 = new Sample(
                    "Do some light stretches.");

            Sample sample33 = new Sample(
                    "Track my water intake.  Drink eight glasses of water " +
                            "over the course of the day.");

            // Environmental
            Sample sample41 = new Sample(
                    "Clean or organize a section of my home.");

            Sample sample42 = new Sample(
                    "On my lunch break, take a break to walk around the " +
                            "block to get exercise and a change of scenery.");

            Sample sample43 = new Sample(
                    "Get a recycling bin for my home.");

            // Financial
            Sample sample51 = new Sample(
                    "Research ways to design a budget that suits me.");

            Sample sample52 = new Sample(
                    "Make my own coffee to bring with me " +
                            "on my commute.");

            Sample sample53 = new Sample(
                    "Check out a thrift store or consignment shop.");

            // Occupational
            Sample sample61 = new Sample(
                    "Create or update my resume.");

            Sample sample62 = new Sample(
                    "Think about where I am in my career and life " +
                            "and research occupations that will work well within " +
                            "that framework.");

            Sample sample63 = new Sample(
                    "Use a day planner or other tracking tool to track " +
                            "and balance my workload.");

            // Social
            Sample sample71 = new Sample(
                    "Volunteer for a cause that I care about.");

            Sample sample72 = new Sample(
                    "Reach out to a friend or loved one just to see " +
                            "how they are doing.");

            Sample sample73 = new Sample(
                    "Make a homemade greeting card or write a " +
                            "thankful note and give it to someone.");

            // associate a dimension with a sample
            sample1.setDimension(dimension1);
            sample2.setDimension(dimension1);
            sample3.setDimension(dimension1);

            sample11.setDimension(dimension2);
            sample12.setDimension(dimension2);
            sample13.setDimension(dimension2);

            sample21.setDimension(dimension3);
            sample22.setDimension(dimension3);
            sample23.setDimension(dimension3);

            sample31.setDimension(dimension4);
            sample32.setDimension(dimension4);
            sample33.setDimension(dimension4);

            sample41.setDimension(dimension5);
            sample42.setDimension(dimension5);
            sample43.setDimension(dimension5);

            sample51.setDimension(dimension6);
            sample52.setDimension(dimension6);
            sample53.setDimension(dimension6);

            sample61.setDimension(dimension7);
            sample62.setDimension(dimension7);
            sample63.setDimension(dimension7);

            sample71.setDimension(dimension8);
            sample72.setDimension(dimension8);
            sample73.setDimension(dimension8);

            // save the samples

            sampleRepository.save(sample1);
            sampleRepository.save(sample2);
            sampleRepository.save(sample3);

            sampleRepository.save(sample11);
            sampleRepository.save(sample12);
            sampleRepository.save(sample13);

            sampleRepository.save(sample21);
            sampleRepository.save(sample22);
            sampleRepository.save(sample23);

            sampleRepository.save(sample31);
            sampleRepository.save(sample32);
            sampleRepository.save(sample33);

            sampleRepository.save(sample41);
            sampleRepository.save(sample42);
            sampleRepository.save(sample43);

            sampleRepository.save(sample51);
            sampleRepository.save(sample52);
            sampleRepository.save(sample53);

            sampleRepository.save(sample61);
            sampleRepository.save(sample62);
            sampleRepository.save(sample63);

            sampleRepository.save(sample71);
            sampleRepository.save(sample72);
            sampleRepository.save(sample73);

            // now refresh the result of the findAll()
            result = dimensionRepository.findAll();

        } // END - sample and dimension population

        // add all dimensions in the dimensionRepository to the model
        model.addAttribute("dimension", result);

        return "index";
    }

}
