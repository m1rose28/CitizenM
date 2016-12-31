package citizenm.citizenm;

/**
 * Created by markrose on 12/30/16.
 */

public class issue {

    private String url, headline, fulltext, age;

        public issue(String url, String headline, String fulltext, String age) {
            this.url = url;
            this.headline = headline;
            this.fulltext = fulltext;
            this.age = age;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHeadline() {
            return headline;
        }

        public void setHeadline(String headline) {
            this.headline = headline;
        }

        public String getFulltext() {
            return fulltext;
        }

        public void setFulltext(String fulltext) {
            this.fulltext = fulltext;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

}



