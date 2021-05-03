
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
			System.out.println("TROZO A COMPROBAR");
			System.out.println(lowerCaseText[i]);
			if(lowerCaseText[i].contains(this.englishTranslation.toLowerCase())) {
				spamCount++;
			}else {
				if(lowerCaseText[i].contains(this.spanishTranslation.toLowerCase())) {
					spamCount++;
				}
			}
		}
		
		System.out.println("is spam");
		System.out.println(spamCount / lowerCaseText.length > 0.1);
		System.out.println("division");
		System.out.println((double)spamCount / lowerCaseText.length);
		System.out.println("numerado");
		System.out.println(spamCount);
		System.out.println("denominador");
		System.out.println(lowerCaseText.length);
		final Double umbral = (double)spamCount / lowerCaseText.length;
		return umbral > 0.1;
	}

}
