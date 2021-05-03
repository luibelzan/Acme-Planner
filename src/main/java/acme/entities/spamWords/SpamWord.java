
package acme.entities.spamWords;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
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

	@Range(min=0, max=1)
	private Double				spamThreshold;


	public boolean isSpam(final String text) {
		final String lowerCaseText = text.toLowerCase().trim().replaceAll("\s+", " ");

		int spamCount = 0;
		for (final String spamWord : this.spanishTranslation.toLowerCase().split(",")) {
			spamCount += StringUtils.countMatches(lowerCaseText, spamWord) * spamWord.length();
		}
		for (final String spamWord : this.englishTranslation.toLowerCase().split(",")) {
			spamCount += StringUtils.countMatches(lowerCaseText, spamWord) * spamWord.length();
		}

		return (float) spamCount / text.length() * 100 > this.spamThreshold;
	}

}
