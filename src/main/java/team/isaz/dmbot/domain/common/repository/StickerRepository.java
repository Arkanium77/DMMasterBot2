package team.isaz.dmbot.domain.common.repository;

import lombok.Getter;
import org.springframework.stereotype.Repository;

@Getter
@Repository
public class StickerRepository {
    private final String[] d20 = new String[]{ //d20 stickers id. From 1 to 20
            "CAADAgADQAADUqjXBObZIbrcPiffAg",
            "CAADAgADQQADUqjXBOWVF6c1Sa_2Ag",
            "CAADAgADQgADUqjXBPE1Ytq-pqdsAg",
            "CAADAgADQwADUqjXBFFdaMBc5LzKAg",
            "CAADAgADRAADUqjXBBTCigPNRSZZAg",
            "CAADAgADRQADUqjXBCaqsGQLGzWxAg",
            "CAADAgADRgADUqjXBIDIXfFz48EOAg",
            "CAADAgADRwADUqjXBGTUnQT_IOtJAg",
            "CAADAgADSAADUqjXBPG9kANpbqVRAg",
            "CAADAgADSQADUqjXBKHsv2IExiCGAg",
            "CAADAgADSgADUqjXBDIH0aBRcMM_Ag",
            "CAADAgADSwADUqjXBIoev7G1pKGcAg",
            "CAADAgADTAADUqjXBD04N-ErLzDnAg",
            "CAADAgADTQADUqjXBHp1mXAyToziAg",
            "CAADAgADTgADUqjXBEFHfmPTsNZhAg",
            "CAADAgADTwADUqjXBPqnhO9t4_-jAg",
            "CAADAgADUAADUqjXBJLshI6MXdi1Ag",
            "CAADAgADUQADUqjXBGmX-o9vmkGKAg",
            "CAADAgADUgADUqjXBISTGJXg9VVwAg",
            "CAADAgADUwADUqjXBC9tLj8RozK4Ag",
    };
    private final String[] coin = new String[]{
            "CAADAgADVAADUqjXBMz71bIdmgEhAg",
            "CAADAgADVQADUqjXBGVDO3J4yuC9Ag"
    };

    private final String manul = "CAADAgADSQEAAvR7GQABDrDY6YiA_i8C";

    public String getD20(int num) {
        return d20[num - 1];
    }

    public String getCoin(int num) {
        return coin[num];
    }
}
