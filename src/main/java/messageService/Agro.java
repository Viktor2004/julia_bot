package messageService;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

public class Agro {
    private int agroLevel;
    private ZonedDateTime lastAgro;

    private List<String> answers = Arrays.asList(
     "Олег, не ругайся!"
     ,"Олег, ну я же просила"
     ,"Я обижусь"
     ,"Все, я с тобой не разговариваю!"
     ,"..."
     ,"🙍"
     ,"👿"
    );

    public Agro() {
        this.agroLevel = 0;
        lastAgro = ZonedDateTime.now();
    }

    public String agro () {
        ZonedDateTime currentMoment = ZonedDateTime.now();
        int cooldownInMins = 5;
        if(currentMoment.isAfter(lastAgro.plusMinutes(cooldownInMins))) {
            agroLevel =1;
            lastAgro = currentMoment;
        } else {
            agroLevel++;
            lastAgro = currentMoment;
        }

        if (agroLevel >= answers.size()) {
            return answers.get(answers.size()-1);
        } else {
            return answers.get(agroLevel-1);
        }

    }

    public int getAgroLevel() {
        return agroLevel;
    }

    public void setAgroLevel(int agroLevel) {
        this.agroLevel = agroLevel;
    }

    public ZonedDateTime getLastAgro() {
        return lastAgro;
    }

    public void setLastAgro(ZonedDateTime lastAgro) {
        this.lastAgro = lastAgro;
    }
}
