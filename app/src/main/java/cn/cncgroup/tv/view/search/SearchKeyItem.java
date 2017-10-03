package cn.cncgroup.tv.view.search;

/**
 * Created by zhangbo on 15-9-15.
 */
public class SearchKeyItem {
    public KeyType type;
    public int icon;
    public String number;
    public String letter1;
    public String letter2;
    public String letter3;
    public String letter4;
    public String desc;

    public SearchKeyItem(KeyType type) {
        this.type = type;
    }

    public SearchKeyItem setIcon(int icon) {
        this.icon = icon;
        return this;
    }

    public SearchKeyItem setNumber(String number) {
        this.number = number;
        return this;
    }

    public SearchKeyItem setLetter1(String letter1) {
        this.letter1 = letter1;
        return this;
    }

    public SearchKeyItem setLetter2(String letter2) {
        this.letter2 = letter2;
        return this;
    }

    public SearchKeyItem setLetter3(String letter3) {
        this.letter3 = letter3;
        return this;
    }

    public SearchKeyItem setLetter4(String letter4) {
        this.letter4 = letter4;
        return this;
    }

    public SearchKeyItem setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public int getLetterCount() {
        int count = 0;
        if (letter1 != null) {
            count++;
        }
        if (letter2 != null) {
            count++;
        }
        if (letter3 != null) {
            count++;
        }
        if (letter4 != null) {
            count++;
        }
        return count;
    }

    public String getLetters() {
        StringBuilder builder = new StringBuilder();
        if (letter1 != null) {
            builder.append(letter1);
        }
        if (letter2 != null) {
            builder.append(letter2);
        }
        if (letter3 != null) {
            builder.append(letter3);
        }
        if (letter4 != null) {
            builder.append(letter4);
        }
        return builder.toString();
    }

    enum KeyType {
        INPUT, DELETE, CLEAR
    }
}
