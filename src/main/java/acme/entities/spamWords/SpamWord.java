
package acme.entities.spamWords;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;

import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SpamWord extends DomainEntity {

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	private String				spanishTranslation;

	@NotBlank
	private String				englishTranslation;

	@Range(min = 0, max = 1)
	private Double				spamThreshold;


	public boolean isSpam(final String text) {
		final String[] lowerCaseText = text.toLowerCase().split(" ");

		int spamCount = 0;
		//		for (final String spamWord : this.spanishTranslation.toLowerCase().split(",")) {
		//			spamCount += StringUtils.countMatches(lowerCaseText, spamWord) * spamWord.length();
		//		}
//		for (final String spamWord : this.englishTranslation.toLowerCase().split(",")) {
//			if (text.contains(spamWord)) {
//				spamCount++;
//			}
//		}
		
		for(int i=0; i<lowerCaseText.length; i++) {
			if(lowerCaseText[i].contains(this.englishTranslation.toLowerCase())) {
				spamCount++;
			}else {
				if(lowerCaseText[i].contains(this.spanishTranslation.toLowerCase())) {
					spamCount++;
				}
			}
		}
		
		
		final Double umbral = (double)spamCount / lowerCaseText.length;
		return umbral > 0.1;
	}

}
