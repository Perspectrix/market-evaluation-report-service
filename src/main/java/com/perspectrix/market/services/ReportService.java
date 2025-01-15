package com.perspectrix.market.services;

import com.perspectrix.market.domain.Person;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Service
public class ReportService {
    double incomeWeight = 0.21;
    double wealthWeight = 0.15;
    double homeValueWeight = 0.24;
    double ageWeight = 0.12;
    double homeOwnershipWeight = 0.27;

    public HashMap<String, String> generateReport(List<Person> people){
        HashMap<String, String> map = new HashMap<>();
        if(people.isEmpty()) return null;
        List<Integer> incomeDis = new ArrayList<>();
        List<Integer> wealthDis = new ArrayList<>();
        List<Integer> homeValueDis = new ArrayList<>();
        List<Short> ageDis = new ArrayList<>();
        List<Short> ownRentDis = new ArrayList<>();

        for (Person p : people) {
            if (p.parseIncomeRange() != null) incomeDis.add(p.parseIncomeRange());
            if (p.parseWealthRange() != null) wealthDis.add(p.parseWealthRange());
            if (p.parseHomeValueRange() != null) homeValueDis.add(p.parseHomeValueRange());
            if(p.parseAgeRange() != null) ageDis.add(p.parseAgeRange());
            if(p.parseOwnRent() != null) ownRentDis.add(p.parseOwnRent());
        }
        double ho = calculateAverageS(ownRentDis);
        double im = calculateMedian(incomeDis);
        double wm = calculateMedian(wealthDis);
        double hvm = calculateMedian(homeValueDis);
        double am = calculateMedianS(ageDis);
        double normIm = normalizeIncome(im);
        double normWm = normalizeWealth(wm);
        double normHvm = normalizeHomeValue(hvm);
        double normAm = normalizeAge(am);


        map.put("Total Number of People", String.valueOf((double) people.size()));
        map.put("Average Income", String.valueOf(calculateAverage(incomeDis)));
        map.put("Median Income", String.valueOf(im));
        map.put("Average Wealth", String.valueOf(calculateAverage(wealthDis)));
        map.put("Median Wealth", String.valueOf(wm));
        map.put("Average Home Value", String.valueOf(calculateAverage(homeValueDis)));
        map.put("Median Home Value", String.valueOf(hvm));
        map.put("Average Age", String.valueOf(calculateAverageS(ageDis)));
        map.put("Median Age", String.valueOf(am));
        map.put("Homeownership Percentage", String.format("%.4g%n", ho*100));
        map.put("Normalized Income Score", String.valueOf(normIm));
        map.put("Normalized Wealth Score", String.valueOf(normWm));
        map.put("Normalized Home Value Score", String.valueOf(normHvm));
        map.put("Normalized Age Score", String.valueOf(normAm));
        map.put("Income weight", String.valueOf(incomeWeight));
        map.put("Wealth Weight", String.valueOf(wealthWeight));
        map.put("Home Value Weight", String.valueOf(homeValueWeight));
        map.put("Age Weight", String.valueOf(ageWeight));
        map.put("Homeownership Weight", String.valueOf(homeOwnershipWeight));
        double score = (ho*homeOwnershipWeight + normIm*incomeWeight + normWm*wealthWeight + normHvm*homeValueWeight + normAm*ageWeight);

        map.put("Competitors", formatCompetitors(getCompetitors(people)));
        map.put("Sample Score", String.valueOf(score));
        return map;
    }

    private String formatCompetitors(List<String> competitors){
        if (competitors.isEmpty()) return "No competitors found";
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < competitors.size(); i++) {
            res.append(competitors.get(i));
            if (i == competitors.size() - 1);
            else { res.append(", ");}
        }
        return res.toString();
    }

    private List<String> getCompetitors(List<Person> people){
        List<String> competitors = new ArrayList<>();
        for (Person person : people) {
            if(!(person.getCompetitors() == null)) {
                for(String competitor : person.getCompetitors()){
                    // If competitor isn't already in the competitors list for that sample, then add them.
                    if (!competitors.contains(competitor)) competitors.add(competitor);
                }
            }
        }
        return competitors;
    }

    /**
     * Normalizes the income value to a value from 0-1. 1 indicating a perfectly ideal income.
     * Max income = 500,000
     * Min income = 10,000
     *
     * @param median
     * Median income of the people in the subset.
     *
     * @return normalized income
     * We utilize a tanh function and our constant, k, which changes to allow for the max value
     * to be 1 with a slow non-linear growth and plateauing decay.
     * Values were gathered using intuition with some assistance from https://www.desmos.com/calculator
     */
    private double normalizeIncome(double median){
        double k = 0.00002;
        return Math.tanh(k*median);
    }

    /**
     * Normalizes the wealth value to a value from 0-1. 1 indicating a perfectly ideal wealth.
     * Max wealth = 15,000,000
     * Min wealth = 250
     *
     * @param median
     * Median wealth of the people in the subset.
     *
     * @return normalized wealth
     * We utilize a tanh function and our constant, k, which changes to allow for the max value
     * to be 1 with a slow non-linear growth and plateauing decay.
     * Values were gathered using intuition with some assistance from https://www.desmos.com/calculator
     */
    private double normalizeWealth(double median){
        double k = 0.00003;
        return Math.tanh(k*median);
    }

    /**
     * Normalizes the home value to a value from 0-1. 1 indicating a perfectly ideal home value.
     * Max home value = 15,000,000
     * Min home value = 250
     *
     * @param median
     * Median home value of the people in the subset.
     *
     * @return normalized home value
     * We utilize a tanh function and our constant, k, which changes to allow for the max value
     * to be 1 with a slow non-linear growth and plateauing decay.
     * Values were gathered using intuition with some assistance from https://www.desmos.com/calculator
     */
    private double normalizeHomeValue(double median){
        double k = 0.00001;
        return Math.tanh(k*median);
    }

    /**
     * Normalizes the age to a value from 0-1. 1 indicating a perfectly ideal age.
     * Max age = 80
     * Min age = 21
     *
     * @param median
     * Median age of the people in the subset.
     *
     * @return normalized age
     * We utilize a tanh function and our constant, k, which changes to allow for the max value
     * to be 1 with a slow non-linear growth and plateauing decay.
     * We make sure to penalize ages that are too high.
     * Values were gathered using intuition with some assistance from https://www.desmos.com/calculator
     */
    private double normalizeAge(double median){
        double k = 0.06;
        if(median > 60) return Math.tanh(k*median)-(0.2*Math.tanh(k*median));
        return Math.tanh(k*median);
    }

    private double calculateAverage(List<Integer> list) {
        if (list == null || list.isEmpty()) return 0.0;
        double avg = 0.0;
        for (int i = 0; i < list.size(); i++) {
            avg += Double.valueOf((list.get(i) - avg) / (i+1));
        }
        return avg;
    }

    private double calculateAverageS(List<Short> list) {
        if (list == null || list.isEmpty()) return 0.0;
        double avg = 0.0;
        for (int i = 0; i < list.size(); i++) {
            avg += Double.valueOf((list.get(i) - avg) / (i+1));
        }
        return avg;
    }

    private double calculateMedian(List<Integer> list) {
        Collections.sort(list);
        return list.get(list.size()/2);
    }

    private double calculateMedianS(List<Short> list) {
        Collections.sort(list);
        return list.get(list.size()/2);
    }
}
