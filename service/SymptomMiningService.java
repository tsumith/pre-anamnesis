package com.sumith.jfs.PaAnaBot.service;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class SymptomMiningService {
    private static final Set<String> MEDICAL_CORPUS = new HashSet<>(Arrays.asList(
            "fever", "headache", "pain", "chest", "cough", "cold", "vomiting",
            "dizziness", "nausea", "bleeding", "stomach", "fatigue", "rash",
            "breathing", "throat", "swelling", "anxiety", "insomnia", "diarrhea",
            "pressure", "sugar", "ache", "burn", "flu", "virus"));
    public String extractDominantSymptoms(List<String> messages) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String msg : messages) {
            String cleanMsg = msg.toLowerCase().replaceAll("[^a-zA-Z ]", "");
            String[] words = cleanMsg.split("\\s+");
            for (String word : words) {
                if (MEDICAL_CORPUS.contains(word)) {
                    frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
                }
            }
        }
        List<Map.Entry<String, Integer>> sortedSymptoms = frequencyMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) 
                .collect(Collectors.toList());
        if (sortedSymptoms.isEmpty())
            return "General Checkup (No specific symptoms detected)";
        return sortedSymptoms.stream()
                .limit(3)
                .map(e -> e.getKey() + " (Count: " + e.getValue() + ")")
                .collect(Collectors.joining(", "));
    }
}
