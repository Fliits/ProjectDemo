package Model;

public enum EventType {
    ARRIVAL_VENUE, //Asiakas saapuu paikalle
    ARRIVAL_VIP_SECURITY, //VIP-asiakas siirtyy VIP-jonoon
    ARRIVAL_GA_SECURITY, //GA-asiakas siirtyy GA-jonoon

    FINISH_VIP_SECURITY, //VIP-asiakas pääsee VIP-turvatarkastuksesta pois
    FINISH_GA_SECURITY, //GA-asiakas pääsee GA-turvatarkastuksesta pois
    FINISH_VIP_CLOAKROOM, //VIP-asiakas poistuu narikasta
    FINISH_GA_CLOAKROOM, //GA-asiakas poistuu narikasta
    FINISH_MERCH, //Asiakas poistuu oheistuotemyyntipisteeltä
    FINISH_ENTER_CONCERT_HALL, //Asiakas on saapunut konserttisaliin ja odottaa konsertin alkua

    START_VIP_SECURITY, //VIP-turvatarkastus alkaa
    START_GA_SECURITY, //GA-turvatarkastus alkaa
    START_VIP_CLOAKROOM, //VIP-narikka alkaa
    START_GA_CLOAKROOM, //GA-narikka alkaa
    START_MERCH, //Oheistuotteiden osto alkaa
    START_ENTER_CONCERT_HALL //Asiakas siirtyy konserttisaliin
}
