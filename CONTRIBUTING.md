The ideal PR
============

Thanks for offering your help.

In order to make our cooperation as smooth as possible, we have a couple of guidelines that we'd like you to follow:

- If in doubt, please discuss your ideas first before providing a pull request. This often helps avoid a lot of unnecessary work.
- Fork the repository.
- Check out and work on your own fork.
- Try to make your commits as atomic as possible. Related changes to three files should be committed in one commit.
- The commit message should reference the issue number, e.g. `[#313] Add Seq.skipLast(int n) and Seq.limitLast(int maxSize)`.
- Try not to modify anything unrelated, i.e. don't make unimportant whitespace / formatting changes, which will just distract during review.

About those `Tuple1<T1>`, `Tuple2<T1, T2>`, ... types
=====================================================

We have a code generator for those. So please, don't go through the hassle of copy-pasting your code 16 times, even if we'd appreciate the effort :)

The code generator is currently not Open Source (it's part of the jOOQ project's internals). If you need to touch *"arity-sensitive"* code in your PR, feel free to just submit examples for degrees 1 and 2, and we'll take it from there! 