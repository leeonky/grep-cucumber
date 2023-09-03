Feature: grep by tag
#  path or file

  Scenario: no tag on feature and do not create file
    Given a feature at "a.feature":
      """
      # language: zh-CN
      功能: 功能
      """
    When grep "a.feature" and specify tag: "@tag"
    Then output should be:
      """
      = []
      """

  Scenario: no tag on feature with only rule and do not create file
    Given a feature at "a.feature":
      """
      # language: zh-CN
      功能: 功能
        Rule: rule
      """
    When grep "a.feature" and specify tag: "@tag"
    Then output should be:
      """
      = []
      """

  Scenario: matches one tag and no scenario
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag
    功能: 功能1
    """
    When grep "a.feature" and specify tag: "@tag"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag
               功能: 功能1
               ```
    """

  Scenario: matches two tags and no scenario
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag1 @tag2
    功能: 功能1
    """
    When grep "a.feature" and specify tag: "@tag2,@tag1"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag1 @tag2
               功能: 功能1
               ```
    """

  Scenario: matches one tag in feature
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag
    功能: 功能1

      场景: 场景1
    """
    When grep "a.feature" and specify tag: "@tag"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag
               功能: 功能1

                 场景: 场景1
               ```
    """

  Scenario: matches two tags in feature and scenario
    Given a feature at "a.feature":
    """
    # language: zh-CN
    @tag1
    功能: 功能1
      @tag2
      场景: 场景1
    """
    When grep "a.feature" and specify tag: "@tag2,@tag1"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               @tag1
               功能: 功能1

                 @tag2
                 场景: 场景1
               ```
    """

  Scenario: matches one tag in scenario
    Given a feature at "a.feature":
    """
    # language: zh-CN
    功能: 功能1

      @tag
      场景: 场景1
    """
    When grep "a.feature" and specify tag: "@tag"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               功能: 功能1

                 @tag
                 场景: 场景1
               ```
    """

  Scenario: matches one tag in scenario under rule
    Given a feature at "a.feature":
    """
    # language: zh-CN
    功能: 功能1

      Rule: rule

        @tag
        场景: 场景1

        场景: 场景2
    """
    When grep "a.feature" and specify tag: "@tag"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               功能: 功能1

                 Rule: rule

                   @tag
                   场景: 场景1
               ```
    """

  Scenario: should remove empty rule when no matches scenario
    Given a feature at "a.feature":
    """
    # language: zh-CN
    功能: 功能1

      Rule: rule

        @tag
        场景: 场景1

      Rule: rule2
        场景: 场景2

      Rule: rule3
        背景: 背景
        场景: 场景3
    """
    When grep "a.feature" and specify tag: "@tag"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               功能: 功能1

                 Rule: rule

                   @tag
                   场景: 场景1
               ```
    """

  Scenario: matches multiple tag groups
    Given a feature at "a.feature":
    """
    # language: zh-CN
    功能: 功能1

      @tag
      场景: 场景1
    """
    When grep "a.feature" and specify tag: "@not" and "@tag"
    Then output should be:
    """
    a.feature: ```
               # language: zh-CN
               功能: 功能1

                 @tag
                 场景: 场景1
               ```
    """

  Scenario: matches one tag in English
    Given a feature at "a.feature":
    """
    @tag
    Feature: feature1
    """
    When grep "a.feature" and specify tag: "@tag"
    Then output should be:
    """
    a.feature: ```
               # language: en
               @tag
               Feature: feature1
               ```
    """

